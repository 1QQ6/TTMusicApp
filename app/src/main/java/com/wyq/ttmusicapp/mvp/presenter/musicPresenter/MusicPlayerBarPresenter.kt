package com.wyq.ttmusicapp.mvp.presenter.musicPresenter

import android.content.Context
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.mvp.model.musicModel.MusicPlayerBarModel
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnMusicPlayerBarListener
import com.wyq.ttmusicapp.mvp.view.MusicPlayerBarView

/**
 * Created by Roman on 2021/1/16
 */
class MusicPlayerBarPresenter(musicPlayerBarView: MusicPlayerBarView) {
    var musicPlayerBarView = musicPlayerBarView
    var musicPlayerBarModel:MusicPlayerBarModel? = null
    init {
        musicPlayerBarModel = MusicPlayerBarModel()
    }

    fun startPlayMusic(context:Context,isNext:Boolean){

        musicPlayerBarModel!!.startOrPauseMusic(context,isNext,object :OnMusicPlayerBarListener{
            override fun getCurrentMusicSuccess(status: Int) {
                musicPlayerBarView.updatePlayerViewUI(status)
            }
            override fun getCurrentMusicError() {
                musicPlayerBarView.startMusicFailed()
            }

            override fun getNextMusicSuccess(songInfo: SongInfo) {
                musicPlayerBarView.setMusicInfo(songInfo)
            }

            override fun getNextMusicError() {
                musicPlayerBarView.startMusicFailed()
            }
        })
    }
}