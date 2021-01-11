package com.wyq.ttmusicapp.utils

import android.app.Application
import android.content.Context

/**
 * Created by Roman on 2021/1/11
 */
class MyApplication: Application() {

    private var context: Context? = null

    companion object{
        @JvmStatic
        var context: Context? = null
            private set
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}