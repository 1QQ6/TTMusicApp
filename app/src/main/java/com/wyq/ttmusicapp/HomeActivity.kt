package com.wyq.ttmusicapp

import android.content.Intent
import android.os.Bundle
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.service.MusicPlayerService
import com.wyq.ttmusicapp.ui.activity.LocalMusicActivity
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startIntent = Intent(this@HomeActivity, MusicPlayerService::class.java)
        startService(startIntent)
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
    }

    override fun initViews() {
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