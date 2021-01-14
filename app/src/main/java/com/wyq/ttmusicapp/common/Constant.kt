package com.wyq.ttmusicapp.common

/**
 * Created by Roman on 2021/1/10
 */
object Constant {
    const val KEY_ID = "id"
    const val KEY_PATH = "path"
    const val KEY_MODE = "mode"
    const val KEY_LIST = "list"
    const val KEY_LIST_ID = "list_id"
    //当前播放条进度，以毫秒为单位
    const val KEY_CURRENT = "current"
    const val KEY_DURATION = "duration"

    const val COMMAND_INIT = 1 //初始化命令
    const val COMMAND_PLAY = 2 //播放命令
    const val COMMAND_PAUSE = 3 //暂停命令
    const val COMMAND_STOP = 4 //停止命令
    const val COMMAND_PROGRESS = 5 //改变进度命令
    const val COMMAND_RELEASE = 6 //退出程序时释放

    const val PLAY_MODE_SEQUENCE_TEXT = "顺序播放"
    const val PLAY_MODE_RANDOM_TEXT = "随机播放"
    const val PLAY_MODE_SINGLE_REPEAT_TEXT = "单曲循环"

    const val COMMAND = "cmd"

    const val STATUS_STOP = 0 //停止状态
    const val STATUS_PLAY = 1 //播放状态
    const val STATUS_PAUSE = 2 //暂停状态
    const val STATUS_RUN = 3 //   状态


    const val LABEL_LOCAL = "本地音乐"

    //歌曲列表常量
    const val LIST_ALL_MUSIC = -1
    const val LIST_MY_LOVE = 10000
    const val LIST_LAST_PLAY = 10001
    const val LIST_DOWNLOAD = 10002
    const val LIST_MY_PLAY = 10003 //我的歌单列表
    const val LIST_PLAYLIST = 10004 //歌单音乐列表

    const val LIST_SINGER = 10005 //歌手
    const val LIST_ALBUM = 10006 //专辑
    const val LIST_FOLDER = 10007 //文件夹
    //扫描结果 0：没有音乐 1：有音乐
    const val HAS_NO_MUSIC = 0
    const val HAS_MUSIC = 1


    //handle常量
    const val SCAN_ERROR = 0
    const val SCAN_COMPLETE = 1
    const val SCAN_UPDATE = 2
    const val SCAN_NO_MUSIC = 3
}