package com.wyq.ttmusicapp.ui.fragment.discovermusic

import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.TopArtistListAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.entity.Artist
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/20
 */
class DiscoverFragment:BaseFragment(),DiscoverContract.View {

    private var presenter:DiscoverContract.Presenter? = null
    private var artists = mutableListOf<Artist>()
    private var mArtistListAdapter: TopArtistListAdapter? = null
    override fun getLayout(): Int {
        return R.layout.fragment_discover
    }

    override fun initData() {
        DiscoverPresenter(this)
        presenter?.start()
        presenter?.loadArtists()
    }

    override fun initViews() {
    }

    override fun showArtistCharts(artists: MutableList<Artist>) {
        this.artists = artists
        initArtistList()

    }

    private fun initArtistList() {
        mArtistListAdapter = TopArtistListAdapter(artists)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        discover_recycler_view.layoutManager = linearLayoutManager
        discover_recycler_view.adapter = mArtistListAdapter
    }

    override fun setPresenter(presenter: DiscoverContract.Presenter) {
        this.presenter = presenter
    }
}