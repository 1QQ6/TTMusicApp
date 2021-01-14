package com.wyq.ttmusicapp.mvp.model.musicModeListener

import android.content.Context
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnScanMusicFinishListener

/**
 * Created by Roman on 2021/1/14
 */
interface IMusicScanModel {
    /**
     * 开始扫描本地音乐
     */
    fun startScanLocalMusic(context: Context, isScanning:Boolean, onScanMusicFinishListener: OnScanMusicFinishListener)
}