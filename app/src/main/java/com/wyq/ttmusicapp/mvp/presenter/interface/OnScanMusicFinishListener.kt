package com.wyq.ttmusicapp.mvp.presenter.`interface`

/**
 * Created by Roman on 2021/1/14
 */
interface OnScanMusicFinishListener {
    /**
     * 扫描本地音乐出错
     */
    fun scanMusicError()

    /**
     * 扫描本地音乐成功
     */
    fun scanMusicSuccess(type:Int)

    /**
     * 正在扫描音乐并更新进度
     */
    fun scanMusicUpdate(path:String,currentProgress:Int)
}