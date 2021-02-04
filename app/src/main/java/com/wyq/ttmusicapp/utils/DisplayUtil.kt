package com.wyq.ttmusicapp.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager

/**
 * @author Jayce
 * @date 2015/6/13
 */
object DisplayUtil {
    /* 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale    （DisplayMetrics类中属性density）
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 兼容9.0刘海屏
     *
     * @param mActivity mActivity
     */
    fun openFullScreenModel(mActivity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val lp = mActivity.window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                mActivity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                mActivity.window.attributes = lp
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun showStatusBar(activity: Activity, show: Boolean) {
        try {
            if (show) {
                val attr = activity.window.attributes
                attr.flags = attr.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                activity.window.attributes = attr
                //                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                val lp = activity.window.attributes
                lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                activity.window.attributes = lp
                //                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}