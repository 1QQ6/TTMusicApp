package com.wyq.ttmusicapp.login

import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.login.bean.Accounts
import com.wyq.ttmusicapp.net.APIService
import com.wyq.ttmusicapp.net.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Roman on 2021/1/26
 */
class LoginTask:LoginContract.Task {

    private var callBack: LoginContract.Presenter.OnLoginCallBack? = null


    override fun login(
        username: String?,
        password: String?,
        onLoginCallBack: LoginContract.Presenter.OnLoginCallBack
    ) {
        callBack = onLoginCallBack
        val mLogin = RetrofitManager.getService(Constant.REQUEST_BASE_URL, APIService.Login::class.java)
        if (username!!.isNotEmpty() && password!!.isNotEmpty()){

            val longCall = mLogin.toLogin(username, password)
            longCall.enqueue(object : Callback<Accounts> {

                override fun onFailure(call: Call<Accounts>, t: Throwable) {
                    callBack?.loginFail("登录失败")
                }

                override fun onResponse(call: Call<Accounts>, response: Response<Accounts>) {

                    var result: Accounts? = response.body()
                    if (result != null && "0" == result.status){
                        callBack?.loginSuccess()
                    }else{
                        callBack?.loginFail(result!!.msg)
                    }
                }
            })
        }
    }
}