package com.wyq.ttmusicapp.ui.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.thinkcool.circletextimageview.CircleTextImageView
import com.wyq.ttmusicapp.R

/**
 * 圆的image自转动画
 */

class RotateCircleImageView @JvmOverloads constructor(internal var context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CircleTextImageView(context, attrs, defStyleAttr) {

    private val rotateAnimator: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)

    init {
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.duration = 20000
        rotateAnimator.repeatMode = RESTART
        rotateAnimator.repeatCount = INFINITE

        setBorderColorResource(R.color.black)
        borderWidth = 5

        //setImageResource(R.drawable.turntable)
    }

    fun start() {
        rotateAnimator.setupStartValues()
        rotateAnimator.start()
    }

    fun pause() {
        rotateAnimator.pause()
    }

    fun resume() {
        rotateAnimator.resume()
    }
}
