package com.wyq.ttmusicapp.utils

import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SingerInfo
import com.wyq.ttmusicapp.entity.SongInfo
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
        singerInfoList.sortBy { it.singerName }
        return singerInfoList
    }

    /**
     * 通过歌手获取歌单
     */
    fun getMusicListBySinger(commonInfo: String): ArrayList<SongInfo> {
        val allMusic = dbManager!!.getAllMusicFromMusicTable()
        val songsList = allMusic.filter { it.musicSinger == commonInfo } as ArrayList<SongInfo>
        songsList.sortByDescending { it.musicName }
        return songsList
    }

    /**
     * 通过专辑id获取歌单
     */
    fun getMusicListByAlbum(commonInfo: String): ArrayList<SongInfo> {
        val allMusic = dbManager!!.getAllMusicFromMusicTable()
        val songsList = allMusic.filter { it.musicAlbumId!!.toString() == commonInfo } as ArrayList<SongInfo>
        songsList.sortByDescending { it.musicName }
        return songsList
    }
}






















