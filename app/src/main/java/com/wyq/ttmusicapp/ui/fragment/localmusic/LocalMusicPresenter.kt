package com.wyq.ttmusicapp.ui.fragment.localmusic

import com.wyq.ttmusicapp.utils.PlayMusicHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/1/28
 */
class LocalMusicPresenter(val view: LocalMusicContract.View): LocalMusicContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun setMusicMode() {

    }

    override fun deleteMusic(musicId: Long) {

    }

    override fun setItem(musicId: Long) {

    }

    override fun requestData() {
        doAsync {
            val allMusic = PlayMusicHelper.getAllMusic()
            Thread.sleep(1500)
            uiThread {
                view.updateListView(allMusic)
            }
        }
    }

    override fun start() {

    }
}