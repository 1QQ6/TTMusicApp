package com.wyq.ttmusicapp.common

/**
 * Created by Roman on 2021/1/10
 */
object Constant {
    //主页tab
    const val TAB_MAIN = 1
    const val TAB_WORK = 2
    const val TAB_ME = 3

    const val KEY_MUSIC_ID = "id"
    const val KEY_MUSIC_PATH = "path"
    const val KEY_PLAY_MODE = "mode"
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

    //播放模式
    const val PLAY_MODE_SEQUENCE = -1
    const val PLAY_MODE_SINGLE_REPEAT = 1
    const val PLAY_MODE_RANDOM = 2

    const val PLAY_MODE_SEQUENCE_TEXT = "顺序播放"
    const val PLAY_MODE_RANDOM_TEXT = "随机播放"
    const val PLAY_MODE_SINGLE_REPEAT_TEXT = "单曲循环"

    const val COMMAND = "cmd"

    const val STATUS_STOP = 0 //停止状态
    const val STATUS_PLAY = 1 //播放状态
    const val STATUS_PAUSE = 2 //暂停状态
    const val STATUS_RUN = 3 //   状态
    const val STATUS_ERROR = 4 // 错误状态


    const val LABEL_LOCAL = "本地音乐"

    //歌曲列表常量
    const val LIST_ALL_MUSIC = -1
    const val LIST_MY_LOVE = 10000
    const val LIST_LAST_PLAY = 10001
    const val LIST_DOWNLOAD = 10002
    const val LIST_MY_PLAY = 10003 //我的歌单列表
    const val LIST_PLAY_LIST = 10004 //歌单音乐列表

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

    val MP_FILTER: String? = "com.example.vinyl.start_mediaplayer"

    var PLAYPRO_EXIT = "com.huwei.intent.PLAYPRO_EXIT_ACTION"
    //关于歌曲信息控件的更新
    const val PLAY_BAR_UPDATE = "com.huwei.intent.PLAYBAR_UPDATE"

    const val PLAY_STATUS_UPDATE = "com.huwei.intent.PLAY_STATUS_UPDATE"
    //当前状态 有关时间控件的更新
    const val CURRENT_UPDATE = "com.huwei.intent.DURATION_UPDATE"
    //在线音乐的缓冲更新
    const val BUFFER_UPDATE = "com.huwei.intent.BUFFER_UPDATE"

    /**
     * 播放服务传递常亮
     */
    const val NOW_PLAY_MUSIC = "NOW_PLAY_MUSIC"
    const val IS_PLAYING = "is_playing"
    const val IS_NEW_PLAY_MUSIC = "is_new_play_music"
    const val SEEK_BAR_CURRENT_TIME = "seek_bar_current_time"

}