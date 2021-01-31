package com.wyq.ttmusicapp.ui.fragment.localmusic

import com.wyq.ttmusicapp.ui.fragment.localmusic.LocalMusicContract

/**
 * Created by Roman on 2021/1/28
 */
class LocalMusicPresenter(val view: LocalMusicContract.View): LocalMusicContract.Presenter {


    override fun setMusicMode() {
        view.setPresenter(this)
    }

    override fun deleteMusic(musicId: Long) {

    }

    override fun setItem(musicId: Long) {

    }

    override fun start() {

    }
}