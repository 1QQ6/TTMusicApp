package com.wyq.ttmusicapp.mvp.presenter.`interface`

import com.wyq.ttmusicapp.mvp.model.entity.SongInfo

/**
 * Created by Roman on 2021/1/16
 */
interface OnMusicPlayerBarListener {
    fun getCurrentMusicSuccess(status:Int)
    fun getCurrentMusicError()
    fun getNextMusicSuccess(songItem:SongInfo)
    fun getNextMusicError()
}