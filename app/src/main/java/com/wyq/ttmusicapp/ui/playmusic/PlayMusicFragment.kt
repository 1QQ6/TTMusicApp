package com.wyq.ttmusicapp.ui.playmusic

import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicFragment :BaseFragment(),PlayMusicContract.View{
    private lateinit var presenter: PlayMusicContract.Presenter
    override fun getLayout(): Int {
        return R.layout.fragment_playing_music
    }

    override fun initData() {

    }

    override fun initViews() {

    }

    override fun togglePanel() {

    }

    override fun setPresenter(presenter: PlayMusicContract.Presenter) {
        this.presenter = presenter

    }
}