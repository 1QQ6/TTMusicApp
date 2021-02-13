package com.wyq.ttmusicapp.musicapi

import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.net.APIManager
import com.wyq.ttmusicapp.utils.MusicUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by Roman on 2021/2/8
 */
object NetEaseApiServiceImpl {
    private val TAG = "NetEaseApiServiceImpl"
    private val apiService by lazy { APIManager.getInstance()?.create(NetEaseApiService::class.java,Constant.BASE_NETEASE_URL) }
    /**
     * 获取热门歌手列表
     */
    fun getTopArtists(limit: Int, offset: Int): Observable<MutableList<Artist>> {
        return apiService?.getTopArtists(offset, limit)!!
            .flatMap { it ->
                Observable.create(ObservableOnSubscribe<MutableList<Artist>> { e ->
                    try {
                        if (it.code == 200) {
                            val list = mutableListOf<Artist>()
                            it.list.artists?.forEach {
                                val playlist = Artist()
                                playlist.artistId = it.id.toString()
                                playlist.singerName = it.name
                                playlist.picUrl = it.picUrl
                                playlist.score = it.score
                                playlist.songsCount = it.musicSize
                                playlist.albumCount = it.albumSize
                                playlist.type = Constant.NET_EASE
                                list.add(playlist)
                            }
                            e.onNext(list)
                            e.onComplete()
                        } else {
                            e.onError(Throwable("网络异常"))
                        }
                    } catch (ep: Exception) {
                        e.onError(ep)
                    }
                })
            }
    }


    /**
     * 获取歌手单曲
     *
     */
    fun getArtistSongs(id: String, offset: Int = 0, limit: Int = 50): Observable<Artist> {
        return apiService?.getArtistSongs(id,offset, limit)!!.flatMap { it->
            Observable.create(ObservableOnSubscribe<Artist> { e ->
                val musicList = arrayListOf<SongInfo>()
                it.songs.forEach {
                    musicList.add(MusicUtils.getMusic(it))
                }
                val artist = Artist()
                artist.songs = musicList
                artist.singerName = it.songs[0].artists?.get(0)?.name
                artist.artistId = it.songs[0].artists?.get(0)?.id
                e.onNext(artist)
                e.onComplete()
            })
        }
    }
}