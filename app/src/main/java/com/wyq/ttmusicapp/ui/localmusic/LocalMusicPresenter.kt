package com.wyq.ttmusicapp.ui.localmusic

/**
 * Created by Roman on 2021/1/28
 */
class LocalMusicPresenter(val view:LocalMusicContract.View):LocalMusicContract.Presenter {


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