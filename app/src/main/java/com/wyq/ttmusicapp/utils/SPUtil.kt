package com.wyq.ttmusicapp.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MusicApplication

/**
 * Created by Roman on 2021/1/11
 */
object SPUtil {
    /**
     *
     */
    @Synchronized
    private fun getSP(): SharedPreferences {
        return MusicApplication.context!!.getSharedPreferences("music", MODE_PRIVATE)
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
    fun saveRecentMusicId(musicId: Long) {
        getSP().edit().putLong(Constant.KEY_MUSIC_ID, musicId).apply()
    }
    @Synchronized
    fun getRecentMusicId():Long{
        return getSP().getLong(Constant.KEY_MUSIC_ID, -1)
    }

    /**
     * 当前音乐播放的进度条
     */
    @Synchronized
    fun saveRecentMusicProgress(currentProgress: Int) {
        getSP().edit().putInt(Constant.KEY_SEEK_BAR_PROGRESS,currentProgress).apply()
    }

    @Synchronized
    fun getRecentMusicProgress():Int{
        return getSP().getInt(Constant.KEY_SEEK_BAR_PROGRESS, -1)
    }

    /**
     * 用户是否登录
     */
    @Synchronized
    fun saveLogin(currentProgress: Boolean) {
        getSP().edit().putBoolean(Constant.IS_LOGIN,currentProgress).apply()
    }

    /**
     * 服务器暂时关闭，splash页面登录功能暂不检查，这里设置为true
     */
    @Synchronized
    fun isLogin():Boolean{
        return getSP().getBoolean(Constant.IS_LOGIN, true)
    }
}