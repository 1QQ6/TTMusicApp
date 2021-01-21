package com.wyq.ttmusicapp.ui.view

import android.R
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.github.xubo.statusbarutils.StatusBarUtils


class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mBackgroundColor: Int
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), StatusBarUtils.getStatusBarHeight(context))
    }

    companion object {
        private val ANDROID_ATTR = intArrayOf(R.attr.background)
        private const val ATTR_ANDROID_BACKCOLOR = 0
    }

    init {
        val ta =
            context.obtainStyledAttributes(attrs, ANDROID_ATTR)
        mBackgroundColor = ta.getColor(
            ATTR_ANDROID_BACKCOLOR,
            Color.TRANSPARENT
        )
        ta.recycle()
        setBackgroundColor(mBackgroundColor)
    }
}