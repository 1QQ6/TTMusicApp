package com.wyq.ttmusicapp.ui.playmusic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.github.xubo.statusbarutils.StatusBarUtils
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity


class PlayMusicActivity : BaseActivity() {

    companion object{
        fun startActivity(context: Context){
            val i = Intent(context, PlayMusicActivity::class.java)
            context.startActivity(i)
            (context as Activity).overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_play_music
    }

    override fun initData() {
        PlayMusicPresenter(baseContext, supportFragmentManager.findFragmentById(R.id.play_fragment) as PlayMusicFragment)
    }

    override fun initViews() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
    override fun setupToolbar() {
        StatusBarUtils.setStatusBarTransparen(this)
    }
}