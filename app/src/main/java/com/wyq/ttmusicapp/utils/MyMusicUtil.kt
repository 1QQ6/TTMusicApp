package com.wyq.ttmusicapp.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.wyq.ttmusicapp.common.Constant

/**
 * Created by Roman on 2021/1/11
 */
object MyMusicUtil {
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


}