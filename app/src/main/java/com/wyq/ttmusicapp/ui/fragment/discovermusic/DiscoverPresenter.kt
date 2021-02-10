package com.wyq.ttmusicapp.ui.fragment.discovermusic

import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.musicapi.NetEaseApiServiceImpl
import com.wyq.ttmusicapp.net.APIManager
import com.wyq.ttmusicapp.net.RequestCallBack

/**
 * Created by Roman on 2021/2/8
 */
class DiscoverPresenter(val view: DiscoverContract.View):DiscoverContract.Presenter {
    init {
        view.setPresenter(this)
    }

    override fun loadArtists() {
        APIManager.getInstance()?.request(NetEaseApiServiceImpl.getTopArtists(30,0),object :RequestCallBack<MutableList<Artist>>{
            override fun success(result: MutableList<Artist>) {
                view.showArtistCharts(result)
            }

            override fun error(msg: String?) {

            }
        })
    }

    override fun start() {

    }
}