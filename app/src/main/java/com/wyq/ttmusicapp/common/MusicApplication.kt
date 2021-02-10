package com.wyq.ttmusicapp.common

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/**
 * Created by Roman on 2021/1/11
 *
 * 这里注意AndroidManifest的 android:name=".utils.MyApplication" 配置
 * 否则onCreate回调不会执行，造成context为空指针异常
 */
class MusicApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        MultiDex.install(this)
    }

    companion object{
        @JvmStatic
        var context: Context? = null
    }
}