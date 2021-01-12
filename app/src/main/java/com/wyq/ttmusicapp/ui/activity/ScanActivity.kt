package com.wyq.ttmusicapp.ui.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.FileNameFormat
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.service.MusicPlayerService
import com.wyq.ttmusicapp.utils.ChineseToEnglish
import com.wyq.ttmusicapp.utils.MyMusicUtil
import kotlinx.android.synthetic.main.activity_scan.*
import java.io.File
import java.lang.ref.WeakReference
import java.util.*


/**
 * Created by Roman on 2021/1/10
 */
class ScanActivity:BaseActivity() {
    private val TAG = ScanActivity::class.java.name
    //是否正在扫描
    private var isScanning = false
    //使用handler来接收扫描结果
    private var handler: Handler? = null
    //扫描进度
    private var currentProgress = 0
    //查询歌曲的信息数组集合
    var musicInfoArray:Array<String>? = null
    //歌单集合
    var musicsList:ArrayList<SongInfo>? = null
    var message:Message? = null
    //数据库管理
    private var dbManager: DatabaseManager? = null
    //当前播放音乐的id
    private var curMusicId :Int? = null

    private var curMusicPath:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbManager = DatabaseManager.getInstance(this)
    }

    override fun getLayout(): Int {
        return R.layout.activity_scan
    }

    override fun initData() {
        musicInfoArray = arrayOf(
            //歌曲名称
            MediaStore.Audio.Media.TITLE,
            //歌曲歌手
            MediaStore.Audio.Media.ARTIST,
            //歌曲的专辑名
            MediaStore.Audio.Media.ALBUM,
            //歌曲时长
            MediaStore.Audio.Media.DURATION,
            //歌曲文件的全路径
            MediaStore.Audio.Media.DATA
        )
    }

    override fun initViews() {
        start_scan_btn.setOnClickListener {
            if (!isScanning){
                isScanning = true
                scan_view.start()
                scan_path.visibility = View.VISIBLE
                startScanLocalMusic()
            }else{
                isScanning = false
                scan_path.visibility = View.GONE
                scan_view.stop()
            }
        }
        handler = MyHandler(this)

    }

    /**
     * 开始扫描本地的音乐
     */
    private fun startScanLocalMusic() {
        Thread{
            var cursor =contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicInfoArray,
                null,
                null,
                null
            )
            try {
                if (cursor?.count!=0){
                    musicsList = java.util.ArrayList()
                    var i = 0
                    while (cursor!!.moveToNext()){
                        if (!isScanning){
                            return@Thread
                        }
                        //查询本地音乐，保存到musicsList中
                        queryLocalMusics(cursor)
                        currentProgress++
                        //发送消息
                        message = Message.obtain()
                        message!!.what = Constant.SCAN_UPDATE
                        message!!.arg1 = cursor.count
                        handler!!.sendMessage(message)
                    }

                    //扫描完成获取当前播放音乐及路径
                    curMusicId = MyMusicUtil.getIntShared(Constant.KEY_ID)
                    curMusicPath = dbManager!!.getMusicPath(curMusicId!!)

                    // 根据a-z进行排序源数据
                    musicsList!!.sortBy { it.musicFirstLetter }
                    dbManager!!.updateAllMusic(musicsList!!)
                    //扫描完成
                    message = Message.obtain()
                    message!!.what = Constant.SCAN_COMPLETE
                    handler!!.sendMessage(message) //更新UI界面

                }else{
                    message = Message.obtain()
                    message!!.what = Constant.SCAN_NO_MUSIC
                    handler!!.sendMessage(message)
                }
            }catch (e:Exception){
                message = Message.obtain()
                message!!.what = Constant.SCAN_ERROR
                handler!!.sendMessage(message)
            }finally {
                cursor?.close()
            }

        }.start()
    }

    private fun queryLocalMusics(cursor: Cursor) {
        var name =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE))
        var singer =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
        var album =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
        var path =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
        val duration =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
        val file = File(path)
        val parentPath = file.parentFile.path

        name = FileNameFormat.replaceUnKnownName(name)
        singer = FileNameFormat.replaceUnKnownName(singer)
        album = FileNameFormat.replaceUnKnownName(album)
        path = FileNameFormat.replaceUnKnownName(path)
        val firstLetter: String =
            ChineseToEnglish.stringToPinyinSpecial(name)?.toUpperCase()?.get(0).toString()

        musicsList?.add(
            SongInfo(
                0,
                name,
                singer,
                album,
                duration,
                path,
                parentPath,
                firstLetter,
                0
            )
        )
    }
    //初始化当前播放音乐，有可能当前正在播放音乐已经被过滤掉了
    private fun initCurPlaying() {
        try {
            var isContain = false
            var id  = 1;
                musicsList?.forEach {
                    if (curMusicPath!=null && it.musicPath.equals(curMusicPath)){
                        isContain = true
                        id = musicsList!!.indexOf(it) + 1
                    }
                }
            if (isContain){
                Log.d(TAG, "initCurPlaying: id = $id")
                MyMusicUtil.setCurrentMusicId(Constant.KEY_ID,id)
            }else{
                Log.d(TAG, "initCurPlaying: !!!contains")
                val intent = Intent(MusicPlayerService.PLAYER_MANAGER_ACTION)
                intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP)
                sendBroadcast(intent)
            }
        }catch (e:java.lang.Exception){

        }
    }

    /**
     * 扫描完毕，点击关闭当前activity
     */
    private fun scanComplete() {
        start_scan_btn.text = "扫描完成"
        isScanning = false
        start_scan_btn.setOnClickListener {
            if (!isScanning){
                finish()
            }
        scan_view.stop()
        }
    }

    override fun setupToolbar() {
        setSupportActionBar(scan_music_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



  inner class MyHandler( activity: ScanActivity?) : Handler() {
        private val weakReference: WeakReference<ScanActivity>?
        override fun handleMessage(msg: Message) {
            if (weakReference?.get() != null) {
                when (msg.what) {
                    Constant.SCAN_NO_MUSIC -> {
                        Toast.makeText(this@ScanActivity, "本地没有歌曲，快去下载吧", Toast.LENGTH_SHORT).show()
                        scanComplete()
                    }
                    Constant.SCAN_ERROR -> {
                        Toast.makeText(this@ScanActivity, "数据库错误", Toast.LENGTH_LONG).show()
                        scanComplete()
                    }
                    Constant.SCAN_COMPLETE -> {
                        initCurPlaying()
                        scanComplete()
                    }
                    Constant.SCAN_UPDATE -> {
                        //                        int updateProgress = msg.getData().getInt("progress");
                        val path = msg.data.getString("scanPath")
                        scan_count.text = "已扫描到" + currentProgress + "首歌曲"
                        scan_path.text = path
                    }
                }
            }
        }

        init {
            weakReference = WeakReference<ScanActivity>(activity)
        }
    }
}