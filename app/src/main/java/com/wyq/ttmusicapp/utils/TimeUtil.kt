package com.wyq.ttmusicapp.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun mill2mmss(duration: Long): String {
        val m: Int
        val s: Int
        var str = ""
        val x = duration.toInt() / 1000
        s = x % 60
        m = x / 60
        if (m < 10) {
            str += "0$m"
        } else {
            str += m
        }
        str += if (s < 10) {
            ":0$s"
        } else {
            ":$s"
        }
        return str
    }

    fun getLrcMillTime(time: String): Int {
        var time = time
        var millTime = 0
        time = time.replace(".", ":")
        val timedata = time.split(":").toTypedArray()

        //Log.i("min,second,mill", timedata[0]+","+timedata[1]+","+timedata[2]);
        var min = 0
        var second = 0
        var mill = 0
        try {
            min = timedata[0].toInt()
            second = timedata[1].toInt()
            mill = timedata[2].toInt()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return -1
        }
        millTime = (min * 60 + second) * 1000 + mill * 10
        return millTime
    }

    private fun getCurrentTime(format: String?): String {
        val date = Date()
        val sdf =
            SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    val currentTime: String
        get() = getCurrentTime("yyyy-MM-dd  HH:mm:ss")
}