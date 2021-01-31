package com.wyq.ttmusicapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman on 2021/1/31
 * name：歌手名称
 * songsCount：歌曲数量
 * albumCount：专辑数量
 */

@Parcelize
data class SingerInfo(var singerName:String,var songsCount:Int,var albumCount:Int) : Parcelable