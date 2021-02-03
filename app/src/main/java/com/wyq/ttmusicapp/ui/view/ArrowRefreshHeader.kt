package com.wyq.ttmusicapp.ui.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.interfaces.IRefreshHeader
import kotlinx.android.synthetic.main.item_refresh_header.view.*

/**
 * Created by Roman on 2021/2/3
 */
class ArrowRefreshHeader: LinearLayout,IRefreshHeader {
    private var TAG = "ArrowRefreshHeaderHeight"
    private var mContentLayout:View? = null
    private var mRotateUpAnim:Animation? = null
    private var mRotateDownAnim:Animation? = null
    private var mMeasuredHeight = 0
    private var mState = 0
    constructor(context: Context?):super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?):super(context,attrs){
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ): super(context, attrs, defStyleAttr){
        //初始化
        init()
    }

    private fun init() {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 0, 0)
        this.layoutParams = layoutParams
        this.setPadding(0, 0, 0, 0)
        //将refreshHeader高度设置为0
        mContentLayout = LayoutInflater.from(context).inflate(R.layout.item_refresh_header, null)
        addView(mContentLayout, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0))
        //初始化动画
        initAnimation()
        //将mContentLayout的LayoutParams高度和宽度设为自动包裹并重新测量
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //获得测量后的高度
        mMeasuredHeight = measuredHeight

    }

    private fun initAnimation(){

        mRotateUpAnim = RotateAnimation(
            0.0f,
            -180.0f,
            Animation.RELATIVE_TO_SELF,
            //对象绕其旋转的点的X坐标
            0.5f,
            Animation.RELATIVE_TO_SELF,
            //对象绕其旋转的点的Y坐标
            0.5f
        )
        mRotateUpAnim?.duration = 200
        //如果fillAfter为true，则此动画执行的转换将在完成后保留。
        mRotateUpAnim?.fillAfter = true

        mRotateDownAnim = RotateAnimation(
            -180f,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        mRotateDownAnim?.duration = 200
        mRotateDownAnim?.fillAfter = true
    }

    private fun setState(state:Int){
        if (state == mState){
            return
        }
        when(state){
            Constant.STATE_NORMAL->{
                if (mState == Constant.STATE_RELEASE_TO_REFRESH){
                    ivHeaderArrow.animation = mRotateDownAnim
                    tvRefreshStatus.text = "下拉刷新"
                }
                if (mState == Constant.STATE_REFRESHING){
                    //取消此视图的所有动画
                    ivHeaderArrow.clearAnimation()
                }
            }
            Constant.STATE_RELEASE_TO_REFRESH->{
                ivHeaderArrow.visibility = View.VISIBLE
                refreshProgress.visibility = View.INVISIBLE
                if (mState != Constant.STATE_RELEASE_TO_REFRESH){
                    ivHeaderArrow.clearAnimation()
                    ivHeaderArrow.startAnimation(mRotateUpAnim)
                    tvRefreshStatus.text = "释放立即刷新"
                }
            }
            Constant.STATE_REFRESHING->{
                ivHeaderArrow.clearAnimation()
                ivHeaderArrow.visibility = View.INVISIBLE
                refreshProgress.visibility = View.VISIBLE
                smoothScrollTo(mMeasuredHeight)
                tvRefreshStatus.text = "正在刷新..."
            }
            Constant.STATE_DONE->{
                ivHeaderArrow.visibility = View.INVISIBLE
                refreshProgress.visibility = View.INVISIBLE
                tvRefreshStatus.text = "刷新完成"
                ivHeaderArrow.clearAnimation()
            }
            else->{

            }
        }
        mState = state
    }

    /**
     * 下拉之后，当正在刷新的时候，将位置从下拉到的位置恢复规定的位置的动画
     *
     * @param destHeight 规定的高度
     */
    private fun smoothScrollTo(destHeight: Int) {
        val animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight)
        Log.i("ArrowRefreshHeader", "smoothScrollTo: " + getVisibleHeight())
        animator.setDuration(300).start()
        animator.addUpdateListener { animation ->
            setVisibleHeight(animation.animatedValue as Int) }
        animator.addListener(object :Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (mState != Constant.STATE_REFRESHING){
                    /*ivHeaderArrow.visibility = View.VISIBLE
                    refreshProgress.visibility = View.INVISIBLE*/
                    tvRefreshStatus.text = "下拉刷新"
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }

    /**
     * 设置刷新头部可见的高度
     *
     * @param height
     */
    private fun setVisibleHeight(height: Int) {
        var height = height
        if (height < 0) height = 0
        val lp =
            mContentLayout!!.layoutParams as LayoutParams
        lp.height = height
        mContentLayout!!.layoutParams = lp
    }

    override fun onReset() {
        setState(Constant.STATE_NORMAL)
    }

    override fun onPrepare() {
        setState(Constant.STATE_RELEASE_TO_REFRESH)
    }

    override fun onRefreshing() {
        setState(Constant.STATE_REFRESHING)
    }

    override fun onMove(offSet: Float, sumOffSet: Float) {
        if (getVisibleHeight() > 0 || offSet > 0) {
            setVisibleHeight(offSet.toInt() + getVisibleHeight())
            // 未处于刷新状态，更新箭头
            if (mState <= Constant.STATE_RELEASE_TO_REFRESH) {
                //手指在屏幕上滑动时，不停地判断，如果大于mMeasuredHeight这个高度，那么就会执行刷新
                //否则就会执行onReset()恢复状态
                if (getVisibleHeight() > mMeasuredHeight) {
                    onPrepare()
                } else {
                    onReset()
                }
            }
        }
    }

    override fun onRelease(): Boolean {
        var isOnRefresh = false
        val height = getVisibleHeight()
        // not visible.
        if (height == 0) {
            isOnRefresh = false
        }
        //如果当前高度大于测量高度，并且状态是刷新之前的状态，则进行刷新
        if (height > mMeasuredHeight && mState < Constant.STATE_REFRESHING) {
            setState(Constant.STATE_REFRESHING)
            isOnRefresh = true
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == Constant.STATE_REFRESHING && height > mMeasuredHeight) {
            smoothScrollTo(mMeasuredHeight)
        }
        //如果下拉刷新取消，则恢复
        if (mState != Constant.STATE_REFRESHING) {
            smoothScrollTo(0)
        }

        if (mState == Constant.STATE_REFRESHING) {
            val destHeight = mMeasuredHeight
            smoothScrollTo(destHeight)
        }
        return isOnRefresh
    }

    override fun refreshComplete() {
        //设置刷新的状态为已完成
        setState(Constant.STATE_DONE)
        //延迟200ms后复位
        Handler().postDelayed({ reset() }, 200)
    }

    private fun reset() {
        smoothScrollTo(0)
        setState(Constant.STATE_NORMAL)
    }

    override fun getHeaderView(): View? {
        return this
    }

    override fun getVisibleHeight(): Int {
        val lp =
            mContentLayout!!.layoutParams as LayoutParams
        Log.e(TAG,lp.height.toString())
        return lp.height
    }

}