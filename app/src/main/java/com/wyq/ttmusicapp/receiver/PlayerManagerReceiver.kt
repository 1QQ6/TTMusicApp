package com.wyq.ttmusicapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.utils.MyMusicUtil
import com.wyq.ttmusicapp.utils.UpdateUIThreadHelper
import java.io.File

/**
 * Created by Roman on 2021/1/11
 */
class PlayerManagerReceiver(context: Context?) : BroadcastReceiver(){

    private val TAG = PlayerManagerReceiver::class.java.name

    val ACTION_UPDATE_UI_ADAPTER =
        "com.wyq.ttmusicapp.receiver.PlayerManagerReceiver:action_update_ui_adapter_broad_cast"

    private var context: Context? = null
    private var databaseManager:DatabaseManager? = null
    private var mediaPlayer:MediaPlayer? = null
    private var threadNumber :Int? = null

    companion object{
        var status: Int = Constant.STATUS_STOP
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun getThreadNumber(): Int {
        return threadNumber!!
    }

    init {

        this.context = context
        databaseManager = DatabaseManager(context!!)
        mediaPlayer = MediaPlayer()
        initMediaPlayer()

    }

    //取一个（0，100）之间的不一样的随机数
    private fun numberRandom() {
        var count: Int
        do {
            count = (Math.random() * 100).toInt()
        } while (count == threadNumber)
        threadNumber = count
    }

    /**
     * 获取当前音乐id和音乐条进度
     * 并根据进度条来设置状态
     */
    private fun initMediaPlayer() {
        // 改变线程号,使旧的播放线程停止
        numberRandom()
        //获取当前音乐id
        val musicId: Int = MyMusicUtil.getIntShared(Constant.KEY_ID)
        val current: Int = MyMusicUtil.getIntShared(Constant.KEY_CURRENT)

        // 如果是没取到当前正在播放的音乐ID，则从数据库中获取第一首音乐的播放信息初始化
        if (musicId == -1) {
            return
        }
        val musicPath = databaseManager?.getMusicPath(musicId) ?: return
        status = if (current == 0) {
            // 设置播放状态为停止
            Constant.STATUS_STOP
        } else {
            // 设置播放状态为暂停
            Constant.STATUS_PAUSE
        }
        MyMusicUtil.setShared(Constant.KEY_ID, musicId)
        MyMusicUtil.setShared(Constant.KEY_PATH, musicPath)

        UpdateUI()
    }

    /**
     * 发送更新UI的广播
     */
    private fun UpdateUI() {

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        //获取到音乐命令，默认为COMMAND_INIT=1
        val cmd = intent!!.getIntExtra(Constant.COMMAND, Constant.COMMAND_INIT)
        when(cmd){
            Constant.COMMAND_INIT ->{
                Log.d(TAG, "COMMAND_INIT")
            }
            //播放状态
            Constant.COMMAND_PLAY ->{
                status = Constant.STATUS_PLAY
                val musicPath = intent.getStringExtra(Constant.KEY_PATH)
                if (musicPath!=null){
                    playMusic(musicPath)
                }else{
                    mediaPlayer!!.start()
                }
            }
            //暂停状态
            Constant.COMMAND_PAUSE->{
                mediaPlayer?.pause()
                status = Constant.STATUS_PAUSE
            }
            //本程序停止状态都是删除当前播放音乐触发
            Constant.COMMAND_STOP->{
                numberRandom()
                status = Constant.STATUS_STOP
                mediaPlayer?.stop()
                initStopOperate()
            }
            //拖动进度
            Constant.COMMAND_PROGRESS->{
                val curProgress = intent.getIntExtra(Constant.KEY_CURRENT, 0)
                //异步的，可以设置完成监听来获取真正定位完成的时候
                mediaPlayer!!.seekTo(curProgress)
            }
            //程序结束释放
            Constant.COMMAND_RELEASE->{
                numberRandom()
                status = Constant.STATUS_STOP
                mediaPlayer?.stop()
                mediaPlayer?.release()
            }
            else->{

            }
        }
        UpdateUI()
    }

    private fun initStopOperate() {
        //MyMusicUtil.setShared(Constant.KEY_ID, databaseManager?.getFirstId(Constant.LIST_ALL_MUSIC))
    }

    private fun playMusic(musicPath: String?) {
        numberRandom()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()

        //在播放过程中到达媒体源末尾时要调用的回调
        mediaPlayer?.setOnCompletionListener {
            numberRandom() //切换线程
            onComplete() //调用音乐切换模块，进行相应操作
            UpdateUI() //更新界面
        }
        try {
            val file = File(musicPath)
            if (!file.exists()){
                Toast.makeText(context, "歌曲文件不存在，请重新扫描", Toast.LENGTH_SHORT).show()
                //MyMusicUtil.playNextMusic(context)
                return
            }
            mediaPlayer?.setDataSource(musicPath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            UpdateUIThreadHelper(this, context!!, threadNumber!!).start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun onComplete() {
        //MyMusicUtil.playNextMusic(context)
    }

}




















