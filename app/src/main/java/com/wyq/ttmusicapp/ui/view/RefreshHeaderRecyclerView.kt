package com.wyq.ttmusicapp.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.apkfuns.logutils.LogUtils
import com.wyq.ttmusicapp.adapter.LocalRVAdapter
import com.wyq.ttmusicapp.adapter.SongRVAdapter
import com.wyq.ttmusicapp.interfaces.IRefreshHeader
import com.wyq.ttmusicapp.interfaces.IRefreshListener

/**
 * Created by Roman on 2021/1/12.
 */
class RefreshHeaderRecyclerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context!!, attrs, defStyle) {
    private var mLastY = -1f
    private var sumOffSet = 0f
    private var mRefreshHeader: IRefreshHeader? = null
    private var mRefreshing = false
    private var mSongAdapter: SongRVAdapter? = null
    private var mRefreshListener: IRefreshListener? = null

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (adapter != null) {
            mSongAdapter = adapter as SongRVAdapter
            mRefreshHeader = ArrowRefreshHeader(context.applicationContext)
            mSongAdapter!!.setRefreshHeader(mRefreshHeader)
        }
        super.setAdapter(adapter)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (mLastY == -1f) {
            mLastY = e.rawY
        }
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = e.rawY
                sumOffSet = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                //为了防止滑动幅度过大，将实际手指滑动的距离除以2
                val deltaY = (e.rawY - mLastY) / 2
                mLastY = e.rawY
                //计算总的滑动的距离
                sumOffSet += deltaY

                if (isOnTop && !mRefreshing) {
                    //移动刷新的头部View
                    mRefreshHeader!!.onMove(deltaY, sumOffSet)
                    if (mRefreshHeader!!.getVisibleHeight() > 0) {
                        return false
                    }
                }
            }
            else -> {
                // reset
                mLastY = -1f
                if (isOnTop && !mRefreshing) {
                    if (mRefreshHeader!!.onRelease()) {
                        //手指松开
                        if (mRefreshListener != null) {
                            mRefreshing = true
                            //回调刷新完成的监听
                            mRefreshListener!!.onRefresh()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }

    private val isOnTop: Boolean
        private get() = mRefreshHeader!!.getHeaderView()!!.parent != null

    fun setOnRefreshListener(listener: IRefreshListener?) {
        mRefreshListener = listener
    }

    fun refreshComplete() {
        if (mRefreshing) {
            mRefreshing = false
            mRefreshHeader!!.refreshComplete()
        }
    }
}