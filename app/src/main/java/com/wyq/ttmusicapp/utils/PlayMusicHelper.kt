package com.wyq.ttmusicapp.utils

import android.content.Context
import android.provider.MediaStore
import com.apkfuns.logutils.LogUtils
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.AlbumInfo
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.ui.fragment.localmusic.LocalMusicContract
import java.io.File


/**
 * Created by Roman on 2021/1/16
 */
object PlayMusicHelper {

    const val PIC_SIZE_SMALL = 0
    const val PIC_SIZE_NORMAL = 1
    const val PIC_SIZE_BIG = 2

    private var dbManager: DatabaseManager? = null
    private const val albumPrePath = "content://media/external/audio/albumart"

    init {
        dbManager = DatabaseManager.getInstance(MusicApplication.context!!)
    }

    fun getMusicInfoById(musicId: Long): SongInfo? {
        return dbManager!!.getSongInfo(musicId)
    }

    fun getAllMusic(): ArrayList<SongInfo> {
        return dbManager!!.getAllMusicFromMusicTable()
    }

    /**
     * 按照歌手分组
     */
    fun getGroupBySingersInfo(): ArrayList<Artist> {
        var singerInfoList = ArrayList<Artist>()
        val allMusic = dbManager!!.getAllMusicFromMusicTable()
        val singerMap = allMusic.groupBy { it.musicSinger }
        singerMap.forEach { (singerName, values) ->
            if (singerName!=null){
                val albumsCount = dbManager!!.getSingerAlbumsCount(singerName)
                var artist = Artist()
                artist.singerName = singerName
                artist.songsCount = values.size
                artist.albumCount = albumsCount.size
                singerInfoList.add(artist)
            }
        }
        singerInfoList.sortBy { it.singerName }
        return singerInfoList
    }

    /**
     * 按照专辑分组
     */
    fun getGroupByAlbumsInfo(): ArrayList<AlbumInfo> {
        var albumInfoList = ArrayList<AlbumInfo>()
        val allMusic = dbManager!!.getAllMusicFromMusicTable()
        val albumMap = allMusic.groupBy { it.musicAlbumId }
        albumMap.forEach {(albumId,songList)->
            albumInfoList.add(AlbumInfo(albumId!!,
                songList[0].musicAlbum.toString(),
                songList[0].coverUrl,
                songList[0].musicSinger.toString(),
                songList.size))
        }
        return albumInfoList
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

    /**
     * 通过id删除music
     */
    fun deleteMusicByID(context: Context,musicId: Long,iLocalDeleteListener: LocalMusicContract.Presenter.ILocalDeleteListener) {
        val musicPath = dbManager?.getMusicPathById(musicId)
        val currentPlayMusicId = PlayMusicManager.getMusicManager()!!.nowPlayingSong!!.music_id
            if (musicPath?.isNotEmpty()!!){
                val file = File(musicPath)
                if (file.exists()){
                    deleteMediaDB(context,file)
                    //删除此抽象路径名表示的文件或目录。如果此路径名表示目录，则该目录必须为空才能删除。
                    val delete = file.delete()
                    LogUtils.d(delete)
                    dbManager?.deleteMusic(musicId)
                    iLocalDeleteListener.onDeleteComplete()
                }
                if(musicId == currentPlayMusicId){

                }
            }else{

                if(musicId == currentPlayMusicId){

                }
            }
    }

    private fun deleteMediaDB(context: Context, file: File) {
        try {
            context.contentResolver.delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Audio.Media.DATA + "= \"" + file.path + "\"",
                null
            )
        }catch (e:Exception){

        }
    }

    /**
     * 设置歌曲喜爱状态
     */
    fun setLocalLoveMusic(musicId: Long) {
        val songInfo = dbManager?.getSongInfo(musicId)
        if (songInfo!=null){
            if (songInfo.musicLove == Constant.NULL_LOVE_STATUS){
                dbManager?.setLocalLove(musicId,Constant.LOVE_STATUS)
            }else{
                dbManager?.setLocalLove(musicId,Constant.NULL_LOVE_STATUS)
            }
        }
    }

    fun getAlbumPic(url: String?,  size: Int): String? {
        println(url)
        val temp = url?.split("?")?.get(0) ?: url
        return when (size) {
            PIC_SIZE_SMALL -> "$temp?param=90y90"
            PIC_SIZE_NORMAL -> "$temp?param=150y150"
            else -> "$temp?param=450y450"
        }
        }
}






















