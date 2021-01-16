package com.wyq.ttmusicapp.ui.fragment

import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment

/**
 * Created by Roman on 2021/1/16
 */
class PlayBarFragment:BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_play_bar
    }

    override fun initData() {

    }

    override fun initViews() {

    }

    companion object {

        @JvmStatic
        @Synchronized
        fun newInstance(): PlayBarFragment {
            return PlayBarFragment()
        }
    }
}