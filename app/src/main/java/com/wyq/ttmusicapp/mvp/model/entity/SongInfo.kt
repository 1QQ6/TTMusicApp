package com.wyq.ttmusicapp.mvp.model.entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * id,歌手，歌曲名，专辑，歌曲长度，歌曲的地址，歌曲大小
 */
@Parcelize
data class SongInfo(
    var music_id: Int?,
    var musicName: String?,
    var musicSinger: String?,
    var musicDuration: String?,
    var musicAlbum: String?,
    var musicPath: String?,
    var musicParentPath: String?,
    var musicFirstLetter: String?,
    var musicLove: Int?):Parcelable
