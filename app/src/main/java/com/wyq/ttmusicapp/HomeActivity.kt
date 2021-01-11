package com.wyq.ttmusicapp

import android.content.Intent
import android.os.Bundle
import com.wyq.ttmusicapp.adpter.SongsAdapter
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.service.MusicPlayerService
import com.wyq.ttmusicapp.ui.activity.LocalMusicActivity
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity() {

    private var listSongInfo:List<SongInfo>? = null
    private var songsAdapter:SongsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startIntent = Intent(this@HomeActivity, MusicPlayerService::class.java)
        startService(startIntent)
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        //listSongs = MusicScanUtils.startScanLocalMusic(this)
    }

    override fun initViews() {
        //songsAdapter = listSongs?.let { SongsAdapter(this, it) }
        //home_list_view.adapter = songsAdapter
        home_local_layout.setOnClickListener {
            val intent = Intent(this@HomeActivity, LocalMusicActivity::class.java)
            startActivity(intent)
        }
        home_recent_layout.setOnClickListener{

        }
    }

    override fun setupToolbar() {

    }

    override fun onDestroy() {
        super.onDestroy()
        val stopIntent = Intent(this@HomeActivity, MusicPlayerService::class.java)
        stopService(stopIntent)
    }
}