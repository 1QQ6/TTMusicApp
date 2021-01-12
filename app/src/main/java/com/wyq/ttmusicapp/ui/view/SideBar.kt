package com.wyq.ttmusicapp.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.wyq.ttmusicapp.R

/**
 * Created by lijunyan on 2017/2/20.
 */
class SideBar : View {
    private var onListener: OnTouchingLetterChangedListener? = null
    private var selected = -1
    private var oldChoose = 0
    private val indexPaint = Paint()
    private val circlePaint = Paint()
    private val textPaint = Paint()
    private val testPaint = Paint()
    private var radius = 0f //显示索引圆形view半径
    private val bounds = Rect() //单个索引字母边界
    private var singleHeight = 0f
    private var curY = 0f //当前y坐标
    private val mTextDialog //显示索引放大的预览View
            : TextView? = null
    private var accentColor = 0

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {

        //设置画预览圆形的画笔
        //获取主题颜色
        val defaultColor = -0x58d68
        val attrsArray = intArrayOf(R.attr.colorAccent)
        val typedArray = context.obtainStyledAttributes(attrsArray)
        accentColor = typedArray.getColor(0, defaultColor)
        typedArray.recycle()
        circlePaint.color = accentColor
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        radius = dp2px(30f).toFloat()

        //设置画预览圆形中索引字母的画笔
        textPaint.color = Color.WHITE
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.textSize = radius * 1.2f
        testPaint.color = Color.WHITE
        testPaint.isAntiAlias = true
        testPaint.style = Paint.Style.STROKE
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: ")

        singleHeight = (height / sections.size).toFloat() // 获取每一个字母的高度
    }

    //为SideBar设置显示索引放大的预览View
    fun setTextView(mTextDialog: TextView?) {
//        this.mTextDialog = mTextDialog;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in sections.indices) {
            val textSize = (singleHeight * 0.7f).toInt()
            indexPaint.color = Color.GRAY
            indexPaint.isAntiAlias = true
            indexPaint.textSize = textSize.toFloat()
            Log.d(TAG, "onDraw: textSize = $textSize")

            // 选中的状态
            if (i == selected) {
                indexPaint.color = accentColor
                indexPaint.isFakeBoldText = true //字体加粗
                val rect = Rect()
                textPaint.getTextBounds(sections[i], 0, 1, rect)
                canvas.drawCircle((width - singleHeight) / 2.0f, curY, radius, circlePaint)

//                canvas.drawLine((width-singleHeight)/2.0f - radius,curY,(width-singleHeight)/2.0f + radius,curY,testPaint);
//                canvas.drawLine((width-singleHeight)/2.0f ,curY- radius,(width-singleHeight)/2.0f ,curY+ radius,testPaint);
//                canvas.drawRect((width-singleHeight)/2.0f - rect.width()/2.0f,curY - rect.height()/2.0f,
//                        (width-singleHeight)/2.0f + rect.width()/2.0f,curY + rect.height()/2.0f,testPaint);
                textPaint.textAlign = Paint.Align.CENTER
                canvas.drawText(
                    sections[i],
                    (width - singleHeight) / 2.0f,
                    curY + rect.height() / 2.0f,
                    textPaint
                )
            }
            indexPaint.getTextBounds(sections[i], 0, 1, bounds)
            val xPos =
                width - singleHeight + singleHeight / 2.0f - indexPaint.measureText(
                    sections[i]
                ) / 2.0f
            val yPos = singleHeight * i + (singleHeight + bounds.height()) / 2.0f
            canvas.drawText(sections[i], xPos, yPos, indexPaint)
            indexPaint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        curY = event.y // 点击y坐标
        oldChoose = selected
        //点击该点前面的索引个数
        val count = (curY / (getHeight() / sections.size)).toInt()
        when (action) {
            MotionEvent.ACTION_UP -> {
                //                setBackgroundColor(Color.parseColor("#00000000"));
                selected = -1
                invalidate()
                if (mTextDialog != null) {
                    mTextDialog.visibility = INVISIBLE
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (event.x < width - singleHeight) return false
                //                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != count) {
                    if (count >= 0 && count < sections.size) {
                        if (onListener != null) {
                            onListener!!.onTouchingLetterChanged(sections[count])
                        }
                        if (mTextDialog != null) {
                            mTextDialog.text = sections[count]
                            mTextDialog.visibility = VISIBLE
                        }
                        selected = count
                        //                        invalidate();
//                        curY = event.getY();
//                        Log.e(TAG, "dispatchTouchEvent: curY = "+curY );
                    }
                }
                curY = if (curY < radius) radius else curY
                curY = if (curY + radius > height) height - radius else curY
                invalidate()
                Log.e(TAG, "dispatchTouchEvent: curY = $curY")
            }
            else -> {
                if (oldChoose != count) {
                    if (count >= 0 && count < sections.size) {
                        if (onListener != null) {
                            onListener!!.onTouchingLetterChanged(sections[count])
                        }
                        if (mTextDialog != null) {
                            mTextDialog.text = sections[count]
                            mTextDialog.visibility = VISIBLE
                        }
                        selected = count
                    }
                }
                curY = if (curY < radius) radius else curY
                curY = if (curY + radius > height) height - radius else curY
                invalidate()
                Log.e(TAG, "dispatchTouchEvent: curY = $curY")
            }
        }
        return true
    }

    //向外公开的方法
    fun setOnListener(
        onListener: OnTouchingLetterChangedListener?
    ) {
        this.onListener = onListener
    }

    //监听接口
    interface OnTouchingLetterChangedListener {
        fun onTouchingLetterChanged(letter: String?)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {
        private val TAG = SideBar::class.java.name

        // 26个字母索引和其他
        var sections = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"
        )
    }
}