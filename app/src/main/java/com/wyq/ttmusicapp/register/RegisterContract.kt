package com.wyq.ttmusicapp.register

import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.login.bean.Accounts

/**
 * Created by Roman on 2021/1/26
 */
interface RegisterContract {

    interface View:BaseView<Presenter>{
        fun getUserName(): String
        fun getPwd(): String
        fun getConfirmPwd(): String
        fun getEmail(): String
        fun registerSuccess(userAccount: Accounts)
        fun registerFail(msg: String)
    }

    interface Presenter:BasePresenter{

        fun register(name: String, pwd: String)
        interface OnRegisterCallBack{
            fun registerSuccess(userAccount:Accounts)
            fun registerFail(message: String)
        }
    }

    interface Task{
        fun goRegister(name: String, pwd: String,onRegisterCallBack: Presenter.OnRegisterCallBack)
    }

}