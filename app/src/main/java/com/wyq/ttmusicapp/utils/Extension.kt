package com.wyq.ttmusicapp.utils

import android.content.Context
import android.widget.Toast

/**
 * 扩展函数类
 */

fun Context.toast(msg : String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}