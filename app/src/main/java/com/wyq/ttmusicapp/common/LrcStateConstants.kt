package com.wyq.ttmusicapp.common

/**
 * 歌词界面状态常量
 * @author Jayce
 * @date 2015/6/10
 */
object LrcStateConstants {
    const val READ_LOC_FAIL = 0 //本地查找不到歌词或者查询异常
    const val READ_LOC_OK = 1
    const val QUERY_ONLINE = 2 //正在联网查找
    const val QUERY_ONLINE_OK = 3
    const val QUERY_ONLINE_NULL = 4 //网络无歌词
    const val QUERY_ONLINE_FAIL = 5 //联网查找失败
}