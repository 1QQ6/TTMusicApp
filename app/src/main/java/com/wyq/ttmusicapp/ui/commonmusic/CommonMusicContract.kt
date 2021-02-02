package com.wyq.ttmusicapp.ui.commonmusic

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.SongInfo

/**
 * Created by Roman on 2021/2/1
 */
interface CommonMusicContract {
    interface View: BaseView<Presenter> {
        fun initListView(musicInfoList:ArrayList<SongInfo>)
        fun showBottomMenu()
    }

    interface Presenter: BasePresenter {
        fun onClickItem(musicId:Long)
        fun loadData(commonInfo:String,type:String)
    }
}