package com.wyq.ttmusicapp.entity
import android.os.Parcel
import android.os.Parcelable

/**
 * id,歌手，歌曲名，专辑，歌曲长度，歌曲的地址，歌曲大小
 */

data class SongInfo(
    //音乐id
    var music_id: Long?,
    //音乐名称
    var musicName: String?,
    //音乐歌手
    var musicSinger: String?,
    //音乐时长
    var musicDuration: Int?,
    //专辑名称
    var musicAlbum: String?,
    //专辑id
    var musicAlbumId: Long?,
    //专辑封面路径
    var coverUrl:String?,
    //音乐路径
    var musicPath: String?,
    //音乐父路径
    var musicParentPath: String?,
    //音乐名称拼音第一个字母
    var musicFirstLetter: String?,
    //音乐喜好
    var musicLove: Int?):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
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
        parcel.writeValue(musicAlbumId)
        parcel.writeString(coverUrl)
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
