package com.wyq.ttmusicapp

import android.os.Bundle
import com.wyq.ttmusicapp.Entity.Song
import com.wyq.ttmusicapp.adpter.SongsAdapter
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.utils.MusicScanUtils
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity() {

    private var listSongs:List<Song>? = null
    private var songsAdapter:SongsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        listSongs = MusicScanUtils.startScanLocalMusic(this)
    }

    override fun initViews() {
        songsAdapter = listSongs?.let { SongsAdapter(this, it) }
        home_list_view.adapter = songsAdapter
    }

    override fun setupToolbar() {

    }
}