package com.wyq.ttmusicapp.interfaces

import com.wyq.ttmusicapp.entity.SongInfo

/**
 * 定义操作播放音乐行为的公共接口
 *
 */
interface IMusicControl {
    //操作相关方法
    fun play()
    fun pause()
    fun stop()
    fun seekTo(mesc: Int)
    fun preparePlayingList(index: Int, list: List<SongInfo?>?)
    fun nextSong()
    fun preSong()
    fun randomSong()

    //状态获取接口
    val isPlaying: Boolean
    val nowPlayingIndex: Int
    val nowPlayingSong: SongInfo?
    val isForeground: Boolean

    //界面控制相关
    fun updateMusicQueue()

    companion object {
        const val MSG_PLAY = 0
        const val MSG_PAUSE = 1
        const val MSG_STOP = 2
        const val MSG_SEEK = 3
        const val MSG_PREPARE = 4
        const val MSG_NEXT_SONG = 5
        const val MSG_PRE_SONG = 6
        const val MSG_RANDOM = 7
    }
}