package com.wyq.ttmusicapp.entity

import android.os.Parcel
import android.os.Parcelable
import com.wyq.ttmusicapp.common.Constant

/**
 * Created by Roman on 2021/1/31
 * name：歌手名称
 * songsCount：歌曲数量
 * albumCount：专辑数量
 */


class Artist() : Parcelable{
    var singerName:String? = null
    var id: Long = 0
    var artistId: String? = null
    var count: Int = 0
    var type: String? = Constant.LOCAL
    var picUrl: String? = null
    var desc: String? = null
    var score: Int = 0
    var songsCount:Int = 0
    var albumCount:Int = 0
    var followed: Boolean = false

    constructor(parcel: Parcel) : this() {
        singerName = parcel.readString()
        id = parcel.readLong()
        artistId = parcel.readString()
        count = parcel.readInt()
        type = parcel.readString()
        picUrl = parcel.readString()
        desc = parcel.readString()
        score = parcel.readInt()
        songsCount = parcel.readInt()
        albumCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(singerName)
        parcel.writeLong(id)
        parcel.writeString(artistId)
        parcel.writeInt(count)
        parcel.writeString(type)
        parcel.writeString(picUrl)
        parcel.writeString(desc)
        parcel.writeInt(score)
        parcel.writeInt(songsCount)
        parcel.writeInt(albumCount)
        parcel.writeByte(if (followed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}