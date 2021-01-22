package com.wyq.ttmusicapp.core

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.MediaPlayer.OnBufferingUpdateListener
import android.media.MediaPlayer.OnCompletionListener
import android.os.*
import android.util.Log
import android.widget.Toast
import com.wyq.ttmusicapp.IMusicControllerService
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import com.wyq.ttmusicapp.utils.TimeUtil
import com.wyq.ttmusicapp.utils.toast
import java.io.File
import kotlin.math.abs
import kotlin.random.Random


/**
 * Created by Roman on 2021/1/18
 */
class MusicControllerService:Service(), OnCompletionListener, OnBufferingUpdateListener {
    private val TAG = MusicControllerService::class.java.name
    private var musicList: MutableList<SongInfo>? = null
    private var musicIndex = 0
    private var mediaPlayer: MediaPlayer? = null
    private var mIsPrepared = false
    private var isPlayCurrentMusic = true
    private var isPrepare = true
    private var controlReceiver:MusicControllerReceiver? = null
    companion object{

        const val MSG_CURRENT = 0
        const val MSG_BUFFER_UPDATE = 1

        const val MSG_NOTIFICATION_UPDATE = 2

        const val MSG_PLAY = 101

        const val PLAY_PRO_EXIT = "com.wyq.intent.PLAY_PRO_EXIT_ACTION"
        const val NEXT_SONG = "com.wyq.intent.action.NEXT_SONG"
        const val PRE_SONG = "com.wyq.intent.action.PRE_SONG"
        const val PLAY_OR_PAUSE = "com.wyq.intent.action.PLAY_OR_PAUSE"
    }
    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var intent: Intent?
            when (msg.what) {
                //音乐进度条更新
                MSG_CURRENT -> {
                    if (!mIsPrepared){
                        return
                    }
                    intent = Intent(Constant.CURRENT_UPDATE)
                    val currentTime: Int = mediaPlayer!!.currentPosition
                    intent.putExtra(Constant.SEEK_BAR_CURRENT_TIME, currentTime)
                    //发送广播更新UI操作
                    sendBroadcast(intent)
                    sendEmptyMessageDelayed(MSG_CURRENT, 500)
                }
                //网络更新
                MSG_BUFFER_UPDATE -> {
                    intent = Intent(Constant.BUFFER_UPDATE)
                    val bufferTime = msg.arg1
                    Log.i("bufferTime", bufferTime.toString() + "")
                    intent.putExtra("bufferTime", bufferTime)
                    sendBroadcast(intent)
                }
                //通知栏消息
                MSG_NOTIFICATION_UPDATE -> {

                }
                //播放消息
                MSG_PLAY -> {
                    val music: SongInfo = msg.obj as SongInfo
                    playMusic(music)
                }
                else->{

                }
            }
        }
    }

    private fun playMusic(music: SongInfo) {
        if (mediaPlayer != null) {
            mIsPrepared = false
            //调用此方法后，将必须通过设置数据源并调用prepare()再次对其进行初始化。
            mediaPlayer!!.reset()
        }
        try {
            val file = File(music.musicPath)
            if (!file.exists()) {
                Toast.makeText(this, "歌曲文件不存在，请重新扫描", Toast.LENGTH_SHORT).show()
                mBinder.nextSong()
                return
            }
            Log.i(TAG, "datasoure:" + music.musicPath)
            //todo 添加网络url
            mediaPlayer!!.setDataSource(music.musicPath)
            //准备播放器以进行异步播放。设置数据源和显示表面后，您需要调用prepare（）或prepareAsync（）。
            // 对于流，应该调用prepareAsync（），该函数立即返回，而不是阻塞直到缓冲了足够的数据。
            mediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
        initMediaPlayer()
        val filter = IntentFilter()
        controlReceiver = MusicControllerReceiver(mBinder,this)
        filter.addAction(PLAY_PRO_EXIT)
        filter.addAction(PRE_SONG)
        filter.addAction(NEXT_SONG)
        filter.addAction(PLAY_OR_PAUSE)
        //filter.addAction(BringToFrontReceiver.ACTION_BRING_TO_FRONT)
        registerReceiver(controlReceiver, filter)
    }

    private fun initMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer!!.reset()
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer!!.setOnCompletionListener(this)
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnPreparedListener {
            mIsPrepared = true
            Log.e(TAG, "setOnPreparedListener:mIsPrepared:$mIsPrepared")
            if (!isPlayCurrentMusic||!this@MusicControllerService.isPrepare){
                //开始或继续播放。如果以前已暂停播放，则将从暂停的位置继续播放。如果播放已停止或之前从未开始过，则播放将从头开始。
                it.start()
                handler.sendEmptyMessage(MSG_CURRENT)
            }
            this.updatePlayState()
        }
    }

    /**
     * 在客户端连接服务端时，Stub通过ServiceConnection传递到客户端
     */
    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind: ")
        return mBinder
    }

    /**
     * 实现接口中暴露给客户端的Stub--Stub继承自Binder，它实现了IBinder接口
     */
    private var mBinder = object :IMusicControllerService.Stub(){

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
        }

        override fun getNowPlayingSong(): SongInfo? {
            return try {
                musicList!![musicIndex]
            }catch (e:java.lang.Exception){

                val recentMusicId = PlayMusicSPUtil.getRecentMusicId()
                PlayMusicHelper.getMusicInfoById(recentMusicId)
            }
        }

        override fun getPid(): Int {
            Log.e(TAG, "getPid: "+Process.myPid())
            return Process.myPid()
        }

        override fun seekTo(mesc: Int) {
            if (!mIsPrepared){
                return
            }
            Log.e(TAG, "seekTo: $mesc")
            mediaPlayer!!.seekTo(mesc)
        }

        override fun preSong() {
            isPlayCurrentMusic = false
            Log.e(TAG, "preSong:isPlayCurrentMusic   $musicIndex")
            musicIndex = if (musicIndex == 0){
                musicList!!.size - 1
            }else{
                abs((musicIndex - 1) % musicList!!.size)
            }
            prepareSong(musicList!![musicIndex])
        }

        override fun play() {

            if (!mIsPrepared) {
                return
            }
           /* reViews.setViewVisibility(R.id.button_play_notification_play, View.GONE)
            reViews.setViewVisibility(
                R.id.button_pause_notification_play,
                View.VISIBLE
            )*/

            //mNoticationManager.notify(NT_PLAYBAR_ID, mNotification)
            Log.e(TAG, "isPlaying:"+mediaPlayer!!.isPlaying.toString())
            Log.e(TAG, "isPlaying:mIsPrepared:$mIsPrepared")
            if (!mediaPlayer!!.isPlaying) {
                isPlayCurrentMusic = true
                mediaPlayer!!.start()
                handler.sendEmptyMessage(MSG_CURRENT)
                this@MusicControllerService.updatePlayState()
            }
        }

        override fun isPlaying(): Boolean {
            Log.e(TAG,
                "isPlaying"+(mIsPrepared && mediaPlayer != null && mediaPlayer!!.isPlaying).toString()
            )
            return mIsPrepared && mediaPlayer != null && mediaPlayer!!.isPlaying
        }

        override fun pause() {
            if (!mIsPrepared){
                return
            }
            Log.e(TAG, "pause:mIsPrepared:$mIsPrepared")
            mediaPlayer!!.pause()
            handler.removeMessages(MSG_CURRENT)
            updatePlayState()
            val currentPosition = mediaPlayer!!.currentPosition
            PlayMusicSPUtil.saveRecentMusicProgress(currentPosition)
        }

        override fun stop() {
            Log.e(TAG, "stop")
        }

        override fun isForeground(): Boolean {
            Log.e(TAG, "isForeground")
            return false
        }

        override fun randomSong() {
            Log.e(TAG, "randomSong")
            val musicIndex = Random.nextInt(musicList!!.size)
            prepareSong(musicList!![musicIndex])
        }

        override fun getPlayingSongIndex(): Int {
            Log.e(TAG, "getPlayingSongIndex")
            return musicIndex
        }

        override fun nextSong() {
            Log.e(TAG, "nextSong:isPlayCurrentMusic   $musicIndex")
            isPlayCurrentMusic = false
            musicIndex = abs((musicIndex + 1) % musicList!!.size)
            prepareSong(musicList!![musicIndex])
        }

        override fun preparePlayingList(musicIndex: Int, list: MutableList<SongInfo>?,isPrepare:Boolean) {
            this@MusicControllerService.musicIndex = musicIndex
            musicList = list
            this@MusicControllerService.isPrepare = isPrepare
            if (musicList == null || musicList!!.isEmpty()) {
                toast("播放列表为空", Toast.LENGTH_LONG)
                return
            }
            Log.e(TAG, "preparePlayingList")
            val song = musicList!![musicIndex]
            prepareSong(song)
        }
    }

    /**
     * 发送广播更新PlayBar的UI
     */
    private fun updatePlayState() {
        val intent = Intent(Constant.PLAY_BAR_UPDATE)
        //是否正在播放
        intent.putExtra(Constant.IS_PLAYING, mediaPlayer!!.isPlaying)
        //正在播放的歌曲信息
        intent.putExtra(Constant.NOW_PLAY_MUSIC, mBinder.nowPlayingSong)
        //正在播放的音乐id
        intent.putExtra(Constant.NOW_PLAY_MUSIC_ID, mBinder.nowPlayingSong?.music_id!!)
        //发送广播
        sendBroadcast(intent)
    }

    private fun prepareSong(music: SongInfo) {
        //Log.d(TAG, "prepareSong music:" + Gson().toJson(music))
        //showMusicPlayerNotification(music)
        //updatePlayState()
        //如果是网络歌曲,而且未从网络获取详细信息，则需要获取歌曲的详细信息
        /*if (music.getType() === AbstractMusic.MusicType.Online) {
            val song: Song = music as Song
            if (!song.hasGetDetailInfo()) {
                //同步请求到歌曲信息
                RetrofitFactory.Companion.create(BaiduMusicService::class.java)
                    .querySong(song.song_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SimpleObserver<SongPlayResp?>() {
                        fun onSuccess(resp: SongPlayResp) {
                            song.bitrate = resp.bitrate
                            song.songinfo = resp.songinfo
                            Log.i(TAG, "song hasGetDetailInfo:$song")
                            updatePlayBar(true, song)
                            val msg = Message.obtain()
                            msg.what = MSG_PLAY
                            msg.obj = song
                            handler.sendMessage(msg)
                            updateArtistView(song)
                        }
                    })
            } else {
                playMusic(music)
            }
        } else {
            playMusic(music)
        }*/
        playMusic(music)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind")
        handler.removeMessages(MSG_CURRENT)
        mediaPlayer?.release()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        unregisterReceiver(controlReceiver)
        super.onDestroy()
    }

    /**
     * 在播放过程中到达媒体源末尾时调用
     */
    override fun onCompletion(mp: MediaPlayer?) {
        try {
            Log.e(TAG, "onCompletion")
            mBinder.nextSong()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    /**
     * 在缓冲通过渐进式HTTP下载接收的媒体流时调用更新状态。
     */
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }

}