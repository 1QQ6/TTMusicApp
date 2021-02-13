package com.wyq.ttmusicapp.utils

import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.musicapi.entity.MusicInfo
import com.wyq.ttmusicapp.utils.PlayMusicHelper.PIC_SIZE_NORMAL
import com.wyq.ttmusicapp.utils.PlayMusicHelper.PIC_SIZE_SMALL

/**
 * Created by Roman on 2021/2/11
 */
object MusicUtils {
    /**
     * 在线歌单歌曲歌曲实体类转化成本地歌曲实体
     */
    fun getMusic(musicInfo: MusicInfo): SongInfo {
        val music = SongInfo()
        if (musicInfo.id != null) {
            music.music_id = musicInfo.id
        }
        music.musicName = musicInfo.name
        music.isOnline = true
        if (musicInfo.artists != null) {
            var artistIds = musicInfo.artists[0].id
            var artistNames = musicInfo.artists[0].name
            for (j in 1 until musicInfo.artists.size - 1) {
                artistIds += ",${musicInfo.artists[j].id}"
                artistNames += ",${musicInfo.artists[j].name}"
            }
            music.musicSinger = artistNames
            music.musicSingerId = artistIds
        }
        music.coverUrl = getAlbumPic(musicInfo.album?.cover,PIC_SIZE_NORMAL)
        return music
    }


    /**
     * 生成不同的图片
     * @param size
     */
    private fun getAlbumPic(url: String?,size: Int): String? {
        println(url)
        val temp = url?.split("?")?.get(0) ?: url
        return when (size) {
                    PIC_SIZE_SMALL -> "$temp?param=90y90"
                    PIC_SIZE_NORMAL -> "$temp?param=150y150"
                    else -> "$temp?param=450y450"
                }
        }
}