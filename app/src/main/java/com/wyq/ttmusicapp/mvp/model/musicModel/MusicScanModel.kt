package com.wyq.ttmusicapp.mvp.model.musicModel

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.FileNameFormat
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.mvp.model.musicModeListener.IMusicScanModel
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnScanMusicFinishListener
import com.wyq.ttmusicapp.ui.activity.ScanActivity
import com.wyq.ttmusicapp.utils.ChineseToEnglish
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import java.io.File
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Roman on 2021/1/14
 */
class MusicScanModel() : IMusicScanModel {
    //查询歌曲的信息数组集合
    var musicInfoArray: Array<String>? = null

    //歌单集合
    private var musicsList: ArrayList<SongInfo>? = null

    //handler发送和接收的消息
    var message: Message? = null

    //数据库管理
    private var dbManager: DatabaseManager? = null

    //使用handler来接收扫描结果
    private var handler: Handler? = null

    //扫描进度
    private var currentProgress = 0

    //当前播放音乐的id
    private var curMusicId: Int? = null

    //当前音乐路径
    private var curMusicPath: String? = null

    override fun startScanLocalMusic(
        context: Context,
        isScanning: Boolean,
        onScanMusicFinishListener: OnScanMusicFinishListener
    ) {
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
        handler = MyHandler(context, onScanMusicFinishListener)
        dbManager = DatabaseManager.getInstance(context)
        startScanLocalMusic(context, isScanning)
    }

    /**
     * 开始扫描本地的音乐
     */
    private fun startScanLocalMusic(context: Context, isScanning: Boolean) {
        Thread {
            var cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicInfoArray,
                null,
                null,
                null
            )
            try {
                if (cursor?.count != 0) {
                    musicsList = ArrayList()
                    //循环查找本地音乐
                    while (cursor!!.moveToNext()) {
                        if (!isScanning) {
                            return@Thread
                        }
                        //查询本地音乐，保存到musicsList中
                        val musicPath = queryLocalMusics(cursor)
                        currentProgress++
                        //发送消息
                        message = Message.obtain()
                        message!!.what = Constant.SCAN_UPDATE
                        message!!.arg1 = cursor.count
                        var data =  Bundle()
                        data.putInt("progress", currentProgress)
                        data.putString("scanPath", musicPath)
                        message!!.data = data
                        handler!!.sendMessage(message)
                        try {
                            Thread.sleep(50)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }

                    //扫描完成获取当前播放音乐及路径
                    curMusicId = PlayMusicSPUtil.getIntShared(Constant.KEY_MUSIC_ID)
                    curMusicPath = dbManager!!.getMusicPath(curMusicId!!)

                    // 根据a-z进行排序源数据
                    musicsList!!.sortBy { it.musicFirstLetter }
                    dbManager!!.updateAllMusic(musicsList!!)
                    //扫描完成
                    message = Message.obtain()
                    message!!.what = Constant.SCAN_COMPLETE
                    handler!!.sendMessage(message) //更新UI界面

                } else {
                    message = Message.obtain()
                    message!!.what = Constant.SCAN_NO_MUSIC
                    handler!!.sendMessage(message)
                }
            } catch (e: Exception) {
                message = Message.obtain()
                message!!.what = Constant.SCAN_ERROR
                handler!!.sendMessage(message)
            } finally {
                cursor?.close()
            }

        }.start()
    }

    private fun queryLocalMusics(cursor: Cursor): String? {
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
        //将扫描的音乐加入到一个list集合，后续将集合的音乐数据插入到数据库中
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

        return path
    }

    //初始化当前播放音乐，有可能当前正在播放音乐已经被过滤掉了
    private fun initCurPlaying(context: Context) {
        try {
            var isContain = false
            var id = 1;
            musicsList?.forEach {
                if (curMusicPath != null && it.musicPath.equals(curMusicPath)) {
                    isContain = true
                    id = musicsList!!.indexOf(it) + 1
                }
            }
            if (isContain) {
                //Log.d(TAG, "initCurPlaying: id = $id")
                PlayMusicSPUtil.setCurrentMusicId(Constant.KEY_MUSIC_ID, id)
            } else {
                //Log.d(TAG, "initCurPlaying: !!!contains")
                /*val intent = Intent(MusicPlayerService.PLAYER_MANAGER_ACTION)
                intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP)
                context.sendBroadcast(intent)*/
            }
        } catch (e: java.lang.Exception) {

        }
    }
    /**
     * 这里采用内部类方式使用弱引用来避免handler内存泄漏
     *
     * 接收到消息后，通过presenter回调给view
     */
    inner class MyHandler(
        val context: Context,
        private val onScanMusicFinishListener: OnScanMusicFinishListener?
    ) : Handler() {
        private val weakReference: WeakReference<ScanActivity>?
        override fun handleMessage(msg: Message) {
            if (weakReference?.get() != null) {
                when (msg.what) {
                    Constant.SCAN_NO_MUSIC -> {
                        onScanMusicFinishListener?.scanMusicSuccess(Constant.HAS_NO_MUSIC)
                    }
                    Constant.SCAN_ERROR -> {
                        onScanMusicFinishListener?.scanMusicError()
                    }
                    Constant.SCAN_COMPLETE -> {
                        initCurPlaying(context)
                        onScanMusicFinishListener?.scanMusicSuccess(Constant.HAS_MUSIC)
                    }
                    Constant.SCAN_UPDATE -> {
                        val path = msg.data.getString("scanPath")
                        onScanMusicFinishListener?.scanMusicUpdate(path!!, currentProgress)
                    }
                }
            }
        }

        init {
            weakReference = WeakReference<ScanActivity>(context as ScanActivity?)
        }
    }


}