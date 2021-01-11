package com.wyq.ttmusicapp.entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * id,歌手，歌曲名，专辑，歌曲长度，歌曲的地址，歌曲大小
 */
@Parcelize
data class SongInfo(
    var music_id: Int?,
    var musicName: String?,
    var singer: String?,
    var duration: String?,
    var albumColumn: String?,
    var path: String?,
    var parentPath: String?,
    var firstLetter: String?,
    var love: Int?):Parcelable
