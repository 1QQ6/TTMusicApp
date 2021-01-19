package com.wyq.ttmusicapp.ui.playmusicbar

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.SongInfo

/**
 * Created by Roman on 2021/1/19
 */
interface PlayBarContract{

    interface View:BaseView<Presenter>{
        /**
         * 更新UI
         */
        fun updatePlayerViewUI(status:Int)

        /**
         * 设置音乐相关信息
         */
        fun setMusicInfo(songItem: SongInfo)

        /**
         * 播放音乐失败
         */
        fun startMusicFailed()
    }
    interface Presenter:BasePresenter{
        /**
         *
         */
        fun startPlayMusic()

        /**
         *
         */
        fun startNextMusic()
    }
}