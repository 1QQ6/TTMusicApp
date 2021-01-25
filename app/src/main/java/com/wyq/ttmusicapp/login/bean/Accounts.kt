package com.wyq.ttmusicapp.login.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


/**
 * Created by Roman on 2021/1/22
 * 登录返回
 */

data class Accounts(val status: String,
                         val msg: String,
                         val result: User):Serializable

data class  User(val id: String, val nickname: String,val password:String,val username:String)