package com.wyq.ttmusicapp.ui.scanmusic

import android.content.Context

/**
 * Created by Roman on 2021/1/20
 */
class ScanPresenter(val view:ScanContract.View):ScanContract.Presenter {
    init {
        view.setPresenter(this)
    }
    override fun startScanMusic(context:Context,isScanning:Boolean) {

        MusicScanHelper.startScanLocalMusic(context!!,isScanning,object :
            OnScanMusicFinishListener {
            override fun scanMusicError() {
                view!!.scanMusicFailed()
            }

            override fun scanMusicSuccess(type: Int) {
                view.scanMusicFinished(type)
            }

            override fun scanMusicUpdate(path: String, currentProgress: Int) {
                view!!.showScanProgress(path, currentProgress)
            }
        })
    }

    override fun start() {

    }
}