package com.wyq.ttmusicapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MyApplication

/**
 * Created by Roman on 2021/1/11
 */
object PlayMusicSPUtil {
    /**
     *
     */
    @Synchronized
    private fun getSP(): SharedPreferences {
        return MyApplication.context!!.getSharedPreferences("music", MODE_PRIVATE)
    }

    /**
     * 获取当前播放的音乐id
     */
    @Synchronized
    fun getIntShared(keyId: String): Int =

        if (keyId == Constant.KEY_CURRENT) {
            getSP().getInt(keyId, 0)
        } else {
            getSP().getInt(keyId, -1)
        }

    /**
     * 获取当前播放的音乐模式
     */
    @Synchronized
    fun getPlayMusicModeShared(): Int =
        getSP().getInt(Constant.KEY_PLAY_MODE, -1)

    /**
     * 获取当前播放的音乐模式
     */
    @Synchronized
    fun setPlayMusicModeShared(value: Int) {
        getSP().edit().putInt(Constant.KEY_PLAY_MODE, value).apply()
    }
    // 设置sharedPreferences
    @Synchronized
    fun setShared(key: String?, value: Int) {
        getSP().edit().putInt(key, value).apply()
    }
    @Synchronized
    fun setShared(key: String?, value: String?) {
        getSP().edit().putString(key, value).apply()
    }

    /**
     * 设置当前播放音乐的id
     */
    @Synchronized
    fun setCurrentMusicId(keyId: String,value:Int){
        getSP().edit().putInt(keyId,value).apply()
    }

    fun saveRecentMusicId(musicId: Int) {
        getSP().edit().putInt(Constant.KEY_MUSIC_ID, musicId).apply()
    }

    fun getRecentMusicId():Int{
        return getSP().getInt(Constant.KEY_MUSIC_ID, -1)
    }
}