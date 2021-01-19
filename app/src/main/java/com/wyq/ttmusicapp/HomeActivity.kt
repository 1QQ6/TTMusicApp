package com.wyq.ttmusicapp

import android.content.Intent
import android.os.Bundle
import com.wyq.ttmusicapp.ui.localmusic.LocalMusicActivity
import com.wyq.ttmusicapp.ui.playmusicbar.PlayBarBaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : PlayBarBaseActivity(),HomeContract.View {

    private var mPresenter: HomeContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_home
    }

    override fun initData() {
        HomePresenter(this,this)
        mPresenter!!.bindMusicController()
        mPresenter!!.start()
    }

    override fun initViews() {
        super.initViews()
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
        mPresenter!!.unbindMusicController()
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        mPresenter = presenter
    }
}