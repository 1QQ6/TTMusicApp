package com.wyq.ttmusicapp.login

import com.wyq.ttmusicapp.login.bean.Accounts
import com.wyq.ttmusicapp.register.RegisterContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/1/26
 */
class LoginPresenter(val view: LoginContract.View):LoginContract.Presenter,LoginContract.Presenter.OnLoginCallBack {
    var mTask: LoginContract.Task? = null
    init {
        view.setPresenter(this)
        mTask = LoginTask()
    }
    override fun goLogin(username: String, password: String) {
        doAsync {
            mTask?.login(username,password,object :LoginContract.Presenter.OnLoginCallBack{

                override fun loginSuccess() {
                    uiThread {
                        view.loginSuccess()
                    }
                }

                override fun loginFail(message: String) {
                    uiThread {
                        view.loginFail(message)
                    }
                }

            })
        }
    }

    override fun start() {

    }

    override fun loginSuccess() {
        view.loginSuccess()
    }

    override fun loginFail(message: String) {
        view.loginFail(message)
    }
}