package com.wyq.ttmusicapp.entity
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * id,歌手，歌曲名，专辑，歌曲长度，歌曲的地址，歌曲大小
 */

data class SongInfo(
    var music_id: Int?,
    var musicName: String?,
    var musicSinger: String?,
    var musicDuration: Int?,
    var musicAlbum: String?,
    var musicPath: String?,
    var musicParentPath: String?,
    var musicFirstLetter: String?,
    var musicLove: Int?):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(music_id)
        parcel.writeString(musicName)
        parcel.writeString(musicSinger)
        parcel.writeValue(musicDuration)
        parcel.writeString(musicAlbum)
        parcel.writeString(musicPath)
        parcel.writeString(musicParentPath)
        parcel.writeString(musicFirstLetter)
        parcel.writeValue(musicLove)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongInfo> {
        override fun createFromParcel(parcel: Parcel): SongInfo {
            return SongInfo(parcel)
        }

        override fun newArray(size: Int): Array<SongInfo?> {
            return arrayOfNulls(size)
        }
    }
}
