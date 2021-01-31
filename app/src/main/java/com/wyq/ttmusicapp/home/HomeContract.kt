package com.wyq.ttmusicapp.home

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView

/**
 * Created by Roman on 2021/1/19
 */
interface HomeContract {

    interface View:BaseView<Presenter>

    interface Presenter:BasePresenter{
        /**
         * 绑定服务的接口
         */
        fun bindMusicController()

        /**
         * 解绑服务的接口
         */
        fun unbindMusicController()

        /**
         * 加载上一个退出时的音乐
         */
        fun loadMusicPlayAgo()
    }
}