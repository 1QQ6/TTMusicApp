package com.wyq.ttmusicapp.utils

import android.os.Build
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SingerInfo
import com.wyq.ttmusicapp.entity.SongInfo
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Roman on 2021/1/16
 */
object PlayMusicHelper {

    private var dbManager: DatabaseManager? = null
    private const val albumPrePath = "content://media/external/audio/albumart"

    init {
        dbManager = DatabaseManager(MusicApplication.context!!)
    }

    fun getMusicInfoById(musicId: Long): SongInfo? {
        return dbManager!!.getSongInfo(musicId)
    }

    /**
     * 按照歌手分组
     */
    fun getGroupBySingersInfo(): ArrayList<SingerInfo> {
        var singerInfoList = ArrayList<SingerInfo>()
        val allMusic = dbManager!!.getAllMusicFromMusicTable()
        val singerMap = allMusic.groupBy { it.musicSinger }
        singerMap.forEach { (singerName, values) ->
            if (singerName!=null){
                val albumsCount = dbManager!!.getSingerAlbumsCount(singerName)
                singerInfoList.add(SingerInfo(singerName,values.size, albumsCount.size))
            }
        }
        return singerInfoList
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






















