package com.wyq.ttmusicapp.core

import android.widget.Toast
import com.wyq.ttmusicapp.IMusicControllerService
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.interfaces.IMusicControl
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import java.io.File
import java.util.concurrent.ExecutorService

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicManager : IMusicControl {

    companion object {

        var musicManagerInstance: PlayMusicManager? = null
        fun getMusicManager(): PlayMusicManager? {
            if (musicManagerInstance == null) {
                musicManagerInstance = PlayMusicManager()
            }
            return musicManagerInstance
        }
    }

    private var service: IMusicControllerService? = null
    private var mPlayThreadPool: ExecutorService? = null

    init {
        mPlayThreadPool = ThreadFactory.get().musicOperateExecutor
    }

    fun bindService(service: IMusicControllerService?) {
        this.service = service
    }

    /**
     * 播放音乐
     */
    override fun play() {
        mPlayThreadPool!!.execute {
            try {
                service!!.play()
            } catch (e: Exception) {

            }
        }
    }

    /**
     * 暂停音乐
     */
    override fun pause() {
        mPlayThreadPool!!.execute {
            try {
                service!!.pause()
            } catch (e: Exception) {

            }
        }
    }

    /**
     * 音乐停止
     */
    override fun stop() {
        mPlayThreadPool!!.execute {
            try {
                service!!.stop()
            } catch (e: Exception) {

            }
        }
    }

    /**
     * 拖动进度条
     */
    override fun seekTo(mesc: Int) {
        mPlayThreadPool!!.execute {
            try {
                service!!.seekTo(mesc)
            } catch (e: Exception) {

            }
        }
    }

    /**
     *
     */
    override fun preparePlayingList(index: Int, list: List<SongInfo?>?) {
        mPlayThreadPool!!.execute {
            try {
                preparePlayingListInner(index, list!!,true)
            } catch (e: Exception) {

            }
        }
    }

    /**
     *
     */
    fun prepareAndPlay(
        index: Int,
        list: List<SongInfo?>?
    ) {
        mPlayThreadPool!!.execute { preparePlayingListInner(index, list!!,false) }
    }

    /**
     *
     */
    private fun preparePlayingListInner(
        index: Int,
        list: List<SongInfo?>,
        isPrepare:Boolean
    ) {
        try {
            service!!.preparePlayingList(index, list,isPrepare)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        /*if (this.list !== list) {
            updateMusicQueue()
            //this.list = list
        }*/
    }

    /**
     * 下一首音乐
     */
    override fun nextSong() {
        mPlayThreadPool!!.execute {
            try {
                service!!.nextSong()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 上一首音乐
     */
    override fun preSong() {
        mPlayThreadPool!!.execute {
            try {
                service!!.preSong()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 随机音乐
     */
    override fun randomSong() {
        mPlayThreadPool!!.execute {
            try {
                service!!.randomSong()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override val isPlaying: Boolean
        get() =  service!!.isPlaying

    override val nowPlayingIndex: Int
        get() = service!!.playingSongIndex

    override val nowPlayingSong: SongInfo?
        get() = service!!.nowPlayingSong

    override val isForeground: Boolean
        get() = service!!.isForeground

    override fun updateMusicQueue() {
        TODO("Not yet implemented")
    }

}