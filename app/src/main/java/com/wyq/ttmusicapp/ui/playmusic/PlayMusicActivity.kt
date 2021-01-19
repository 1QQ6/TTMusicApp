package com.wyq.ttmusicapp.ui.playmusic

import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity


class PlayMusicActivity : BaseActivity() {


    override fun getLayout(): Int {
        return R.layout.activity_play_music
    }

    override fun initData() {
        PlayMusicPresenter(baseContext, supportFragmentManager.findFragmentById(R.id.play_fragment) as PlayMusicFragment)
    }

    override fun initViews() {
    }

    override fun setupToolbar() {

    }
}