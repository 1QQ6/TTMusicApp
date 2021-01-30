package com.wyq.ttmusicapp.ui.localmusic

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView

/**
 * Created by Roman on 2021/1/28
 */
interface LocalMusicContract {

    interface View:BaseView<Presenter>{
        fun updateListView()
        fun showBottomMenu()
    }

    interface Presenter:BasePresenter{
        fun setMusicMode()
        fun deleteMusic(musicId:Long)
        fun setItem(musicId:Long)
    }

}