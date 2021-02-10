package com.wyq.ttmusicapp.musicapi

import com.wyq.ttmusicapp.musicapi.entity.ArtistsInfo
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

}