package com.wyq.ttmusicapp.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.login.bean.Accounts
import com.wyq.ttmusicapp.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class MusicRegisterActivity : BaseActivity(),RegisterContract.View {
    private var registerPresenter:RegisterContract.Presenter? = null

    companion object{
        fun startActivity(ctx: Context){
            val i = Intent(ctx, MusicRegisterActivity::class.java)
            ctx.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_register
    }

    override fun initData() {
        RegisterPresenter(this)
    }

    override fun initViews() {
        btn_register.setOnClickListener {
            goRegister()
        }
    }

    private fun goRegister() {
        if (checkRegister()){
            registerPresenter!!.register(getUserName(),getPwd())
        }
    }

    private fun checkRegister(): Boolean{
        if (TextUtils.isEmpty(getUserName())){
            input_name.requestFocus()
            input_name.error = getString(R.string.username_cant_null)
            return false
        }
        if (TextUtils.isEmpty(getPwd())){
            input_pwd.requestFocus()
            input_pwd.error = getString(R.string.password_cant_null)
            return false
        }
        if (getPwd() != getConfirmPwd()){
            input_confirm_pwd.requestFocus()
            input_confirm_pwd.error = getString(R.string.pwd_not_confirm)
            return false
        }
        if (TextUtils.isEmpty(getEmail())){
            input_email.requestFocus()
            input_email.error = getString(R.string.email_cant_null)
            return false
        }
        return true
    }

    override fun setupToolbar() {

    }

    override fun getUserName(): String {
        return input_name.text.toString()
    }

    override fun getPwd(): String {
        return input_pwd.text.toString()
    }

    override fun getConfirmPwd(): String {
        return input_confirm_pwd.text.toString()
    }

    override fun getEmail(): String {
        return input_email.text.toString()
    }

    override fun registerSuccess(userAccount: Accounts) {
        toast("注册成功!${userAccount.result.username}${userAccount.result.password}")
        finish()
    }

    override fun registerFail(msg: String) {
        toast("注册失败,${msg}!")
    }

    override fun setPresenter(presenter: RegisterContract.Presenter) {
        registerPresenter = presenter
    }
}