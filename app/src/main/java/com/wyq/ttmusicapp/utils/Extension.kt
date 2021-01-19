package com.wyq.ttmusicapp.utils

import android.content.Context
import android.widget.Toast

/**
 * 扩展函数类
 */

fun Context.toast(msg : String, duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}