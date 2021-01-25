package com.wyq.ttmusicapp.base

import android.content.Context
import com.wyq.ttmusicapp.common.MyApplication.Companion.context
import com.wyq.ttmusicapp.utils.toast
import org.jetbrains.anko.toast
import java.io.Serializable

/**
 * Created by Roman on 2021/1/26
 */
class BaseBean<T> :Serializable {

    var data: T? = null

    //	SUCCESS
    var resultCode : String? = null
    //	成功,系统处理正常
    var resultDesc: String? = null

    fun isSuccess(): Boolean {
        return "SUCCESS" == resultCode
    }

    fun showErrorMsg() {
        context?.toast(resultDesc.toString())
    }
}