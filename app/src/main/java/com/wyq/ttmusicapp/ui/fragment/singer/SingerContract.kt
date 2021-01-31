package com.wyq.ttmusicapp.ui.fragment.singer

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.SingerInfo

/**
 * Created by Roman on 2021/1/31
 */
interface SingerContract {

    interface View:BaseView<Presenter>{
        fun updateListView()
        fun showBottomMenu()
        fun getSingersData(singerList: ArrayList<SingerInfo>)
    }

    interface Presenter:BasePresenter{
        fun onClickMenu(musicId:Long)
        fun onClickLove(musicId:Long)
    }
}