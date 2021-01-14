package com.wyq.ttmusicapp.mvp.presenter.musicPresenter

import android.content.Context
import com.wyq.ttmusicapp.mvp.model.musicModel.MusicScanModel
import com.wyq.ttmusicapp.mvp.model.musicModeListener.IMusicScanModel
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnScanMusicFinishListener
import com.wyq.ttmusicapp.mvp.view.MusicView

/**
 * Created by Roman on 2021/1/14
 */
class MusicScanPresenter(musicView: MusicView) {

    var musicScanModel: IMusicScanModel? = null
    var musicView: MusicView? = musicView

    init {
        musicScanModel = MusicScanModel()
    }

    /**
     * scanPresenter连接scanModel和scanView
     */
    fun startScanMusic(context: Context, isScanning: Boolean) {

        musicScanModel!!.startScanLocalMusic(
            context,
            isScanning,
            object : OnScanMusicFinishListener {

                override fun scanMusicError() {
                    musicView!!.scanMusicError()
                }

                override fun scanMusicSuccess(type: Int) {
                    musicView!!.scanMusicSuccess(type)
                }

                override fun scanMusicUpdate(path: String, currentProgress: Int) {
                    musicView!!.showScanProgress(path, currentProgress)
                }

            })
    }
}