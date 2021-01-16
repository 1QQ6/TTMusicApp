package com.wyq.ttmusicapp.mvp.view

import com.wyq.ttmusicapp.mvp.model.entity.SongInfo

/**
 * Created by Roman on 2021/1/16
 */
interface MusicPlayerBarView {
    //更新UI
    fun updatePlayerViewUI(status:Int)
    //设置音乐相关信息
    fun setMusicInfo(songItem:SongInfo)
    //播放音乐失败
    fun startMusicFailed()
}