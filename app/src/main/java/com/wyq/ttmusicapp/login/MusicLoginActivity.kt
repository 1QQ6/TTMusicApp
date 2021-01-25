package com.wyq.ttmusicapp.login

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.wyq.ttmusicapp.HomeActivity
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.register.MusicRegisterActivity
import com.wyq.ttmusicapp.utils.SPUtil
import com.wyq.ttmusicapp.utils.toast
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Roman on 2021/1/22
 */
class MusicLoginActivity:BaseActivity(),LoginContract.View {

    var loginPresenter:LoginContract.Presenter? = null

    companion object{
        fun startActivity(ctx: Context){
            val i = Intent(ctx, MusicLoginActivity::class.java)
            ctx.startActivity(i)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        LoginPresenter(this)
    }

    override fun initViews() {
        login.setOnClickListener {
            userToLogin()
        }
        register.setOnClickListener {
            MusicRegisterActivity.startActivity(this)
        }
    }

    private fun userToLogin() {
        if (checkUserInfo()){
            loginPresenter?.goLogin(getUserName(),getPwd())
        }

    }

    private fun checkUserInfo(): Boolean {
        if (TextUtils.isEmpty(getUserName())){
            username.requestFocus()
            username.error = getString(R.string.username_cant_null)
            return false
        }
        if (TextUtils.isEmpty(getPwd())){
            password.requestFocus()
            password.error = getString(R.string.password_cant_null)
            return false
        }
        return true
    }

    override fun setupToolbar() {

    }

    override fun getUserName(): String {
        return username.text.toString()
    }

    override fun getPwd(): String {
        return password.text.toString()
    }

    override fun loginSuccess() {
        toast(getString(R.string.login_success))
        SPUtil.saveLogin(true)
        HomeActivity.startActivity(this)
        finish()
    }

    override fun loginFail(msg: String) {
        toast("登录失败，${msg}")
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        loginPresenter = presenter
    }

}