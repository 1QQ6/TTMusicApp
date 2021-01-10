package com.wyq.ttmusicapp.base

import androidx.fragment.app.Fragment

/**
 * Created by Roman on 2021/1/10
 */
abstract class BaseFragment:Fragment() {

    abstract fun getLayout(): Int

    abstract fun findViews()

    abstract fun initViews()
}