package com.wyq.ttmusicapp.ui.fragment.discovermusic

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.Artist

/**
 * Created by Roman on 2021/2/8
 */
interface DiscoverContract {

    interface View:BaseView<Presenter>{
        fun showArtistCharts(artists: MutableList<Artist>)
    }

    interface Presenter:BasePresenter{
        fun loadArtists()
    }

}