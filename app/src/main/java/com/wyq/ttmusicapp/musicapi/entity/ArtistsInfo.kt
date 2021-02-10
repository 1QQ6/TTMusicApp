package com.wyq.ttmusicapp.musicapi.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Roman on 2021/2/8
 */
data class ArtistsInfo(@SerializedName("code")
                       val code: Int = 0,
                       @SerializedName("list")
                       val list: List)


data class ArtistInfo(
                        /**
                        * 最新排名
                        */
                      @SerializedName("lastRank")
                      val lastRank: Int = 0,
                        /**
                        * 图片地址
                        */
                      @SerializedName("img1v1Url")
                      val imgVUrl: String = "",
                        /**
                         * 音乐大小
                         */
                      @SerializedName("musicSize")
                      val musicSize: Int = 0,
                        /**
                         * 图片id
                         */
                      @SerializedName("img1v1Id")
                      val imgVId: Long = 0,
                        /**
                         * 专辑大小
                         */
                      @SerializedName("albumSize")
                      val albumSize: Int = 0,
                        /**
                         *
                         */
                      @SerializedName("picUrl")
                      val picUrl: String = "",
                        /**
                         *
                         */
                      @SerializedName("score")
                      val score: Int = 0,
                        /**
                         *
                         */
                      @SerializedName("topicPerson")
                      val topicPerson: Int = 0,
                        /**
                         *
                         */
                      @SerializedName("briefDesc")
                      val briefDesc: String = "",
                        /**
                         *
                         */
                      @SerializedName("name")
                      val name: String = "",
                        /**
                         *
                         */
                      @SerializedName("id")
                      val id: Int = 0,
                        /**
                         *
                         */
                      @SerializedName("picId")
                      val picId: Long = 0,
                        /**
                         *
                         */
                      @SerializedName("trans")
                      val trans: String = "")


data class List(@SerializedName("artists")
                val artists: MutableList<ArtistInfo>?,
                @SerializedName("updateTime")
                val updateTime: Long = 0,
                @SerializedName("type")
                val type: Int = 0)