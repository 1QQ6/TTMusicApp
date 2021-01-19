package com.wyq.ttmusicapp.ui.playmusicbar

import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.ui.playmusicbar.PlayBarFragment

/**
 * Created by Roman on 2021/1/16
 */
open class PlayBarBaseActivity:BaseActivity() {

    var playBarFragment: PlayBarFragment? = null
    override fun getLayout(): Int {
        return 0
    }


    override fun initData() {

    }

    override fun initViews() {
        showPlayBar()
    }

    private fun showPlayBar() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (playBarFragment == null){
            playBarFragment = PlayBarFragment.newInstance()
            fragmentTransaction.add(R.id.fragment_play_bar, playBarFragment!!).commitAllowingStateLoss()
        }else{
            fragmentTransaction.show(playBarFragment!!).commitAllowingStateLoss()
        }
    }

    override fun setupToolbar() {

    }
}