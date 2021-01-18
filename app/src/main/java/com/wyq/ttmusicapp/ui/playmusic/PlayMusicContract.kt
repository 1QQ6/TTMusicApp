package com.wyq.ttmusicapp.ui.playmusic

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicContract {

    interface View:BaseView<Presenter>{
        //切换面板
        fun togglePanel()
    }
    interface Presenter:BasePresenter{
        //面板点击
        fun performPanelClick()
    }

}