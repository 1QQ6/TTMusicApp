package com.wyq.ttmusicapp.interfaces

import android.view.View

/**
 * Created by Roman on 2021/2/3
 *
 * recycleView 下拉刷新接口
 */
interface IRefreshHeader {


    fun onReset()

    /**
     * 处于可以刷新的状态，已经过了指定距离
     */
    fun onPrepare()
    /**
     * 正在刷新
     */
    fun onRefreshing()
    /**
     * 下拉移动
     */
    open fun onMove(offSet: Float, sumOffSet: Float)

    /**
     * 下拉松开
     */
    fun onRelease(): Boolean

    /**
     * 下拉刷新完成
     */
    fun refreshComplete()

    /**
     * 获取HeaderView
     */
    fun getHeaderView(): View?

    /**
     * 获取Header的显示高度
     */
    fun getVisibleHeight(): Int
}