package com.wyq.ttmusicapp.register

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
class RegisterTask : RegisterContract.Task {
    private var callback: RegisterContract.Presenter.OnRegisterCallBack? = null

    override fun goRegister(
        name: String,
        pwd: String,
        onRegisterCallBack: RegisterContract.Presenter.OnRegisterCallBack
    ) {
        callback = onRegisterCallBack

        val registerService =
            RetrofitManager.getService(Constant.REQUEST_BASE_URL, APIService.Register::class.java)
        val registerCallBack = registerService.toRegister(name, pwd)
        registerCallBack.enqueue(object : Callback<Accounts> {

            override fun onFailure(call: Call<Accounts>, t: Throwable) {
                callback?.registerFail("注册失败")
            }

            override fun onResponse(call: Call<Accounts>, response: Response<Accounts>) {
                var result: Accounts? = response!!.body()
                if (result != null && "0" == result.status) {
                    callback?.registerSuccess(result)
                } else {
                    callback?.registerFail(result!!.msg)
                }
            }
        })
    }
}