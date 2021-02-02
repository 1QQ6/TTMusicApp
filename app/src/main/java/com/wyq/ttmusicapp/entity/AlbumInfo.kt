package com.wyq.ttmusicapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman on 2021/2/2
 * 专辑id，专辑名称，歌手，
 */
@Parcelize
data class AlbumInfo(var albumId:Long,
                     var albumName:String,
                     val albumUrl:String?,
                     var singer:String,
                     var songCount:Int):Parcelable