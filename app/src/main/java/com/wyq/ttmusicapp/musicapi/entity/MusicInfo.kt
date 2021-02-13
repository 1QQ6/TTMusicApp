package com.wyq.ttmusicapp.musicapi.entity

import kotlin.collections.List

/**
 * Created by Roman on 2021/2/10
 */

import com.google.gson.annotations.SerializedName

data class ArtistsItem(@SerializedName("id")
                       val id: String = "",
                       @SerializedName("name")
                       val name: String = "")

data class Album(@SerializedName("id")
                 val id: String? = "",
                 @SerializedName("name")
                 val name: String? = "",
                 @SerializedName("pic_str")
                 val cover: String? = "")

data class MusicInfo(@SerializedName("id")
                     var id: Long?,
                     @SerializedName("name")
                     val name: String? = "",
                     @SerializedName("ar")
                     val artists: List<ArtistsItem>?,
                     @SerializedName("al")
                     val album: Album?
                     )

data class QualityBean(@SerializedName("192")
                       val high: Boolean = false,
                       @SerializedName("320")
                       val hq: Boolean = false,
                       @SerializedName("999")
                       val sq: Boolean = false)

data class CollectBatchBean(@SerializedName("ids")
                            val ids: List<String>? = null,
                            @SerializedName("vendor")
                            val vendor: String? = null)

data class CollectBatch2Bean(@SerializedName("collects")
                             val collects: MutableList<CollectDetail>? = null)

data class CollectDetail(@SerializedName("id")
                         val id: String,
                         @SerializedName("vendor")
                         val vendor: String)

data class CollectResult(@SerializedName("failedList")
                         val failedList: List<CollectFailed>?)

data class CollectFailed(@SerializedName("id")
                         val id: String?,
                         @SerializedName("msg")
                         val msg: String?)



