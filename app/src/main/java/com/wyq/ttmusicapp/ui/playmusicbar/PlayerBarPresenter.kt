package com.wyq.ttmusicapp.ui.playmusicbar

import android.content.Context
import com.wyq.ttmusicapp.core.PlayMusicManager

/**
 * Created by Roman on 2021/1/16
 */
class PlayerBarPresenter(view: PlayBarContract.View):PlayBarContract.Presenter{
    init {
        view.setPresenter(this)
    }

    override fun startPlayMusic() {
        if (PlayMusicManager.getMusicManager()!!.isPlaying){

            PlayMusicManager.getMusicManager()!!.pause()

        }else{

            PlayMusicManager.getMusicManager()!!.play()

        }

    }

    override fun startNextMusic() {
        PlayMusicManager.getMusicManager()!!.nextSong()
    }

    override fun start() {

    }
}