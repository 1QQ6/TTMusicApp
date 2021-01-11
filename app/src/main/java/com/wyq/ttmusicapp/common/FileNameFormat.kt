package com.wyq.ttmusicapp.common

import android.util.Log

/**
 * Created by Roman on 2021/1/10
 */
object FileNameFormat {

    fun replaceUnKnownName(name:String):String{
        var replaceName = ""
        try {
            replaceName = if (name == "<unknown>") {
                name.replace("<unknown>".toRegex(), "未知")
            }else{
                name
            }
        } catch (e: Exception) {

        }
        return replaceName
    }
}