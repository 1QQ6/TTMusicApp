package com.wyq.ttmusicapp.ui.scanmusic

import android.content.Context
import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView

/**
 * Created by Roman on 2021/1/20
 */
interface ScanContract {

    interface View:BaseView<Presenter>{
        fun scanMusicFinished(type:Int)
        fun scanMusicFailed()
        fun showScanProgress(path:String,currentProgress:Int)
    }

    interface Presenter:BasePresenter{
        fun startScanMusic(context: Context, isScanning:Boolean)
    }
}