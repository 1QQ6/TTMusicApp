package com.wyq.ttmusicapp.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import com.wyq.ttmusicapp.service.MusicPlayerService

/**
 * Created by Roman on 2021/1/11
 *
 * 这里注意AndroidManifest的 android:name=".utils.MyApplication" 配置
 * 否则onCreate回调不会执行，造成context为空指针异常
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val startIntent = Intent(this@MyApplication, MusicPlayerService::class.java)
        startService(startIntent)
    }

    companion object{
        @JvmStatic
        var context: Context? = null
    }

}