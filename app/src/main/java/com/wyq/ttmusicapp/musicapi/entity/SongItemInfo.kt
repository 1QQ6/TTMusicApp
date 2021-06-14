package com.wyq.ttmusicapp.musicapi.entity

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

/**
 * Created by Roman on 2021/2/13
 */
data class SongItemInfo(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("br")
    val br: String? = "",
    @SerializedName("size")
    val size: Long?,
    @SerializedName("md5")
    val md5: String?,
    @SerializedName("freeTrialPrivilege")
    val freeTrialPrivilege:FreeTrialPrivilege

)

data class FreeTrialPrivilege(
    @SerializedName("resConsumable")
    var resConsumable: Boolean?,
    @SerializedName("userConsumable")
    val userConsumable: Boolean?
)

data class NetEasySongInfo(
    @SerializedName("data")
    var songItemInfo: List<SongItemInfo>?,
    @SerializedName("code")
    val code: String? = ""
)