package com.wyq.ttmusicapp.musicapi.entity

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

/**
 * Created by Roman on 2021/2/10
 */
data class AlbumData(@SerializedName("data")
                     val data: AlbumBean,
                     @SerializedName("status")
                     val status: Boolean = false,
                     @SerializedName("msg")
                     val msg: String = "")

data class AlbumBean(@SerializedName("name")
                     val name: String = "",
                     @SerializedName("cover")
                     val cover: String? = null,
                     @SerializedName("desc")
                     val desc: String? = null,
                     @SerializedName("publishTime")
                     val publishTime: Long,
                     @SerializedName("artist")
                     val artist: ArtistItem,
                     @SerializedName("songs")
                     val songs: List<MusicInfo>
)

data class ArtistSongs(@SerializedName("more")
                       val more: Boolean,
                       @SerializedName("total")
                       val total: Int,
                       @SerializedName("code")
                       val code: Int,
                       @SerializedName("songs")
                       val songs: List<MusicInfo>
)


data class ArtistItem(@SerializedName("name")
                      val name: String = "",
                      @SerializedName("id")
                      val id: String = "",
                      @SerializedName("cover")
                      val cover: String? = null,
                      @SerializedName("desc")
                      val desc: String? = null)