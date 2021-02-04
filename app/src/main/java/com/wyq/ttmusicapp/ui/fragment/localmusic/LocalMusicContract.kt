package com.wyq.ttmusicapp.ui.fragment.localmusic

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.SongInfo

/**
 * Created by Roman on 2021/1/28
 */
interface LocalMusicContract {

    interface View:BaseView<Presenter>{
        fun updateListView(musicList: ArrayList<SongInfo>)
        fun showBottomMenu()
    }

    interface Presenter:BasePresenter{
        fun setMusicMode()
        fun deleteMusic(musicId:Long)
        fun setItem(musicId:Long)
        fun requestData()
    }

}