package com.wyq.ttmusicapp.ui.fragment.album

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.AlbumInfo

/**
 * Created by Roman on 2021/2/2
 */
interface AlbumContract {

    interface View:BaseView<Presenter>{
        fun updateListView()
        fun showBottomMenu()
        fun initAlbumData(albumList: ArrayList<AlbumInfo>)
    }

    interface Presenter:BasePresenter{
        fun onClickMenu(musicId:Long)
        fun onItemClick(musicId:Long)
    }
}