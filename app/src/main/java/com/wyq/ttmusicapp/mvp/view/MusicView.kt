package com.wyq.ttmusicapp.mvp.view

/**
 * Created by Roman on 2021/1/14
 */

interface MusicView {
    /**
     * 扫描音乐进度
     */
    fun showScanProgress(path:String,currentProgress:Int)
    /**
     * 扫描音乐成功
     */
    fun scanMusicSuccess(type:Int)
    /**
     * 扫描失败
     */
    fun scanMusicError()
}