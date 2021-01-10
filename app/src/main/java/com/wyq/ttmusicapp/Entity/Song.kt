package com.wyq.ttmusicapp.Entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * 歌手，歌曲名，歌曲的地址，歌曲长度，歌曲大小
 */
@Parcelize
data class Song(
    var singer: String? = null,
    var songName:String?= null,
    var path:String?= null,
    var duration:Int,
    var size:Long):Parcelable
