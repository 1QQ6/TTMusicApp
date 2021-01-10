package com.wyq.ttmusicapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Roman on 2021/1/10
 */
abstract class BaseFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun getLayout(): Int

    abstract fun initData()

    abstract fun initViews()
}