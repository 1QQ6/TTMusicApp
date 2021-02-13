package com.wyq.ttmusicapp.entity
import android.os.Parcel
import android.os.Parcelable

/**
 * id,歌手，歌曲名，专辑，歌曲长度，歌曲的地址，歌曲大小
 */

class SongInfo():Parcelable {
    //音乐id
    var music_id: Long? = null
    //音乐名称
    var musicName: String? = null
    //音乐歌手
    var musicSinger: String?= null
    //[网络]音乐歌手Id
    var musicSingerId: String?= null
    //音乐时长
    var musicDuration: Int? = 0
    //专辑名称
    var musicAlbum: String?= null
    //专辑id
    var musicAlbumId: Long?= null
    //专辑封面路径
    var coverUrl:String?= null
    //音乐路径
    var musicPath: String?= null
    //音乐父路径
    var musicParentPath: String?= null
    //音乐名称拼音第一个字母
    var musicFirstLetter: String?= null
    //音乐喜好
    var musicLove: Int?= 0
    // [本地|网络]
    var isOnline: Boolean = true
    // 文件名
    var fileName: String? = null
    // 文件大小
    var fileSize: Long = 0
    // 发行日期
    var year: String? = null
    //更新日期
    var date: Long = 0

    constructor(parcel: Parcel) : this() {
        music_id = parcel.readValue(Long::class.java.classLoader) as? Long
        musicName = parcel.readString()
        musicSinger = parcel.readString()
        musicSingerId = parcel.readString()
        musicDuration = parcel.readValue(Int::class.java.classLoader) as? Int
        musicAlbum = parcel.readString()
        musicAlbumId = parcel.readValue(Long::class.java.classLoader) as? Long
        coverUrl = parcel.readString()
        musicPath = parcel.readString()
        musicParentPath = parcel.readString()
        musicFirstLetter = parcel.readString()
        musicLove = parcel.readValue(Int::class.java.classLoader) as? Int
        isOnline = parcel.readByte() != 0.toByte()
        fileName = parcel.readString()
        fileSize = parcel.readLong()
        year = parcel.readString()
        date = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(music_id)
        parcel.writeString(musicName)
        parcel.writeString(musicSinger)
        parcel.writeString(musicSingerId)
        parcel.writeValue(musicDuration)
        parcel.writeString(musicAlbum)
        parcel.writeValue(musicAlbumId)
        parcel.writeString(coverUrl)
        parcel.writeString(musicPath)
        parcel.writeString(musicParentPath)
        parcel.writeString(musicFirstLetter)
        parcel.writeValue(musicLove)
        parcel.writeByte(if (isOnline) 1 else 0)
        parcel.writeString(fileName)
        parcel.writeLong(fileSize)
        parcel.writeString(year)
        parcel.writeLong(date)
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
