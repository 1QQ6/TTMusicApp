package com.wyq.ttmusicapp.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by Roman on 2021/1/10
 */
class ViewPagerScroller : ViewPager {


    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {}

    private var isSliding = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (!isSliding) {
            false
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (!isSliding) {
            false
        } else {
            super.onTouchEvent(ev)
        }
    }

    fun setSliding(sliding: Boolean) {
        isSliding = sliding
    }
}