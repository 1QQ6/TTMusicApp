package com.wyq.ttmusicapp.musicapi

import android.provider.SyncStateContract
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.net.APIManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by Roman on 2021/2/8
 */
object NetEaseApiServiceImpl {
    private val TAG = "NetEaseApiServiceImpl"
    private val apiService by lazy { APIManager.getInstance()?.create(NetEaseApiService::class.java,Constant.BASE_NETEASE_URL) }
    /**
     * 获取歌单歌曲
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
}