package com.wyq.ttmusicapp.net

/**
 * Des    : 请求成功回调类
 * Author : master.
 * Date   : 2018/5/27 .
 */
interface RequestCallBack<T> {
    fun success(result: T)
    fun error(msg: String?)
}