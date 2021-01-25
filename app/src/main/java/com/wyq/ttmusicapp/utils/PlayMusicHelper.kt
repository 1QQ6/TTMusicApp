package com.wyq.ttmusicapp.utils

import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MyApplication
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import java.util.*


/**
 * Created by Roman on 2021/1/16
 */
object PlayMusicHelper {

    private var dbManager: DatabaseManager? = null
    private const val albumPrePath = "content://media/external/audio/albumart"

    init {
        dbManager = DatabaseManager(MyApplication.context!!)
    }

    /**
     * 操作当前播放音乐状态
     */
    /*fun playCurrentMusic(context: Context): Int {
        val musicId: Int = PlayMusicSPUtil.getIntShared(Constant.KEY_MUSIC_ID)
        if (musicId == -1 || musicId == 0) {
            val intent = Intent(Constant.MP_FILTER)
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP)
            context.sendBroadcast(intent)
            Toast.makeText(context, "歌曲不存在", Toast.LENGTH_SHORT).show()
            return Constant.STATUS_ERROR
        }
        //如果当前媒体在播放音乐状态，则图片显示暂停图片，按下播放键，则发送暂停媒体命令，图片显示播放图片。以此类推。
        when (getPlayStatus()) {
            //当前暂停状态-->发送广播到播放状态
            Constant.STATUS_PAUSE -> {
                //sendMusicStatusBroadcast(context, "", Constant.COMMAND_PLAY)
                return Constant.STATUS_PLAY
            }
            //当前播放状态-->发送广播到暂停状态
            Constant.STATUS_PLAY -> {
                //sendMusicStatusBroadcast(context, "", Constant.COMMAND_PAUSE)
                return Constant.STATUS_PAUSE
            }
            else -> {
                //为停止状态时发送播放命令，并发送将要播放歌曲的路径
                val musicPath = dbManager!!.getMusicPath(musicId)
                //sendMusicStatusBroadcast(context, musicPath!!, Constant.COMMAND_PLAY)
                return Constant.STATUS_PLAY
            }
        }
    }*/

    /*fun getPlayStatus(): Int {
        return PlayerManagerReceiver.status
    }*/

    /**
     * 发送广播
     */
    /*private fun sendMusicStatusBroadcast(context: Context, musicPath: String, command: Int) {
        val intent = Intent(MusicPlayerService.PLAYER_MANAGER_ACTION)
        intent.putExtra(Constant.COMMAND, command)
        if (musicPath.isNotEmpty()) {
            intent.putExtra(Constant.KEY_MUSIC_PATH, musicPath)
        }
        context.sendBroadcast(intent)
    }*/

    /**
     * 播放下一首音乐
     */
    /*fun playNextMusic(context: Context): SongInfo? {
        //获取当前播放模式
        val playMode: Int = PlayMusicSPUtil.getPlayMusicModeShared()
        //获取当前音乐id
        var musicId = PlayMusicSPUtil.getIntShared(Constant.KEY_MUSIC_ID)
        //获取当前播放列表
        val currentPlayList = getCurrentPlayList()
        val musicIdList = ArrayList<Int>()
        currentPlayList.forEach {
            musicIdList.add(it.music_id!!)
        }
        val nextMusicId = getNextMusicId(musicIdList, musicId, playMode)
        PlayMusicSPUtil.setShared(Constant.KEY_MUSIC_ID, nextMusicId)
        if (nextMusicId == -1) {
            var intent = Intent(MusicPlayerService.PLAYER_MANAGER_ACTION)
            intent.putExtra(Constant.COMMAND, Constant.COMMAND_STOP)
            context.sendBroadcast(intent)
            return null
        }
        //发送播放下一首歌的广播
        val musicPath = dbManager!!.getMusicPath(nextMusicId)
        //sendMusicStatusBroadcast(context, musicPath!!, Constant.COMMAND_PLAY)
        return getMusicInfoById(nextMusicId)
    }*/

    fun getMusicInfoById(musicId: Long): SongInfo? {
        return dbManager!!.getSongInfo(musicId)
    }


    /**
     * 获取下首歌的id
     */
    private fun getNextMusicId(musicIdList: ArrayList<Int>, musicId: Int, playMode: Int): Int {
        var id = musicId
        if (id == -1) {
            return -1
        }
        var currentIndex = musicIdList.indexOf(id)
        if (currentIndex == -1) {
            return -1
        }
        when (playMode) {
            Constant.PLAY_MODE_SEQUENCE -> {
                //如果当前音乐是最后一首，返回第一首歌曲
                id = if (currentIndex + 1 == musicIdList.size) {
                    musicIdList[0]
                } else {
                    musicIdList[++currentIndex]
                }
            }
            Constant.PLAY_MODE_RANDOM -> {
                id = getRandomMusic(musicIdList, id)
            }
            Constant.PLAY_MODE_SINGLE_REPEAT -> {

            }
        }

        return id
    }

    /**
     * 获取随机歌曲id
     */
    private fun getRandomMusic(list: ArrayList<Int>, id: Int): Int {
        var musicId: Int
        if (id == -1) {
            return -1
        }
        if (list.isEmpty()) {
            return -1
        }
        if (list.size == 1) {
            return id
        }
        do {
            val count = (Math.random() * list.size).toInt()
            musicId = list[count]
        } while (musicId == id)
        return musicId
    }

    /**
     * 获取当前音乐列表数据
     */
    private fun getCurrentPlayList(): List<SongInfo> {
        //获取列表类型
        val playListMode: Int = SPUtil.getIntShared(Constant.KEY_LIST)
        var musicInfoList: List<SongInfo> = ArrayList<SongInfo>()
        when (playListMode) {
            Constant.LIST_ALL_MUSIC -> {
                musicInfoList = dbManager!!.getAllMusicFromMusicTable()
                return musicInfoList
            }
            Constant.LIST_MY_LOVE -> {

            }
            Constant.LIST_LAST_PLAY -> {

            }
            Constant.LIST_MY_PLAY -> {

            }
            Constant.LIST_PLAY_LIST -> {

            }
            Constant.LIST_SINGER -> {

            }
            Constant.LIST_ALBUM -> {

            }
            Constant.LIST_FOLDER -> {

            }
        }
        return musicInfoList
    }
}






















