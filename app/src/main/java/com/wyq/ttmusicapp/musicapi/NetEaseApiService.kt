package com.wyq.ttmusicapp.musicapi

import com.wyq.ttmusicapp.musicapi.entity.ArtistSongs
import com.wyq.ttmusicapp.musicapi.entity.ArtistsInfo
import com.wyq.ttmusicapp.musicapi.entity.NetEasySongInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Roman on 2021/2/8
 */
interface NetEaseApiService {
    /**
     * 获取排行榜前 limit 名歌手信息
     */
    @GET("/toplist/artist")
    fun getTopArtists(@Query("offset") offset:Int ,@Query("limit") limit:Int):Observable<ArtistsInfo>

    /**
     * 歌手全部歌曲
     */
    @GET("/artist/songs")
    fun getArtistSongs(@Query("id") id: String, @Query("offset") offset: Int,@Query("limit") limit: Int): Observable<ArtistSongs>
    /**
     * 获取音乐的url
     */
    @GET("/song/url")
    fun getSongUrl(@Query("id") id: String):Observable<NetEasySongInfo>
}