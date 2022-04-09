package com.wyq.ttmusicapp.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.wyq.ttmusicapp.R

/**
 * Created by lijunyan on 2017/3/7.
 */
class ScanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    /**
     * 画圆的笔
     */
    private var circlePaint: Paint? = null

    /**
     * 画扇形渲染的笔
     */
    private var sectorPaint: Paint? = null

    /**
     * 扫描线程
     */
    private var mThread: ScanThread? = null

    /**
     * 线程进行标志
     */
    private var threadFlag = false

    /**
     * 线程启动标志
     */
    private var start = false

    /**
     * 扇形转动的角度
     */
    private var angle = 0

    /**
     * 当前视图的宽高，这里相同
     */
    private var viewSize = 0

    /**
     * 画在view中间的图片
     */
    private var bitmap: Bitmap? = null

    /**
     * 对图形进行处理的矩阵类
     */
    private var accentColor = 0
    private val circlrColor1 //第一圈圆环颜色
            : String
    private val circlrColor2 //第二圈圆环颜色
            : String
    private val circlrColor3 //第三圈圆环颜色
            : String
    private val color: String

    /**
     * 此处设置viewSize固定值为500
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewSize = 500
        setMeasuredDimension(viewSize, viewSize)
    }

    /**
     * 从资源中解码bitmap
     */
    private fun initBitmap() {
        val drawable = tintDrawable(
            ContextCompat.getDrawable(context,R.drawable.music_note),
            ColorStateList.valueOf(accentColor)
        )
        bitmap = drawableToBitamp(drawable)
    }

    private fun drawableToBitamp(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mPaint = Paint()
        mPaint.isAntiAlias = true
        val rect =
            Rect(0, 0, bitmap!!.width, bitmap!!.height)
        val rectd = Rect(
            viewSize / 10 * 4,
            viewSize / 10 * 4,
            viewSize - viewSize / 10 * 4,
            viewSize - viewSize / 10 * 4
        )
        canvas.drawBitmap(bitmap!!, rect, rectd, mPaint)
        canvas.drawCircle(
            viewSize / 2.toFloat(),
            viewSize / 2.toFloat(),
            viewSize / 10 * 2.5f,
            circlePaint!!
        )
        circlePaint!!.color = Color.parseColor(circlrColor2)
        canvas.drawCircle(
            viewSize / 2.toFloat(),
            viewSize / 2.toFloat(),
            viewSize / 10 * 3.5f,
            circlePaint!!
        )
        circlePaint!!.color = Color.parseColor(circlrColor3)
        canvas.drawCircle(
            viewSize / 2.toFloat(),
            viewSize / 2.toFloat(),
            viewSize / 10 * 4.5f,
            circlePaint!!
        )
        if (threadFlag) {
            canvas.concat(matrix)
            canvas.drawCircle(
                viewSize / 2.toFloat(),
                viewSize / 2.toFloat(),
                viewSize / 10 * 3.5f,
                sectorPaint!!
            )
        }
    }

    fun start() {
        mThread = ScanThread(this)
        mThread!!.start()
        threadFlag = true
        start = true
    }

    fun stop() {
        if (start) {
            threadFlag = false
            start = false
            invalidate()
        }
    }

    internal inner class ScanThread(private val view: ScanView) : Thread() {
        override fun run() {
            super.run()
            while (threadFlag) {
                if (start) {
                    view.post {
                        angle = angle + 5
                        if (angle == 360) {
                            angle = 0
                        }
                        //matrix = Matrix()
                        matrix!!.setRotate(
                            angle.toFloat(),
                            viewSize / 2.toFloat(),
                            viewSize / 2.toFloat()
                        )
                        view.invalidate()
                    }
                    try {
                        sleep(20)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    fun tintDrawable(drawable: Drawable?, colors: ColorStateList?): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }

    companion object {
        private const val TAG = "ScanView"
        fun colorToHexString(color: Int): String {
            val colorHex: String
            Log.d(TAG, "colorToHexString: color = $color")
            colorHex = if (color < 16) {
                "0" + Integer.toHexString(color)
            } else {
                Integer.toHexString(color)
            }
            return colorHex
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.theme)
        if (typedArray != null) {
            accentColor = typedArray.getColor(
                R.styleable.theme_accent_color,
                resources.getColor(R.color.colorPrimary)
            )
            typedArray.recycle()
        }
        color =
            colorToHexString(Color.red(accentColor)) + colorToHexString(
                Color.green(accentColor)
            ) +
                    colorToHexString(Color.blue(accentColor))
        circlrColor1 = "#D0$color"
        circlrColor2 = "#70$color"
        circlrColor3 = "#30$color"
        Log.d(TAG, "ScanView: accentColor = $accentColor")
        Log.d(TAG, "ScanView: color = $color")
        Log.d(TAG, "ScanView: circlrColor1 = $circlrColor1")
        initBitmap()

        circlePaint = Paint()
        circlePaint!!.style = Paint.Style.STROKE
        circlePaint!!.isAntiAlias = true
        circlePaint!!.strokeWidth = (viewSize / 10).toFloat()
        circlePaint!!.color = Color.parseColor(circlrColor1)

        sectorPaint = Paint()
        sectorPaint!!.isAntiAlias = true
        sectorPaint!!.style = Paint.Style.STROKE
        sectorPaint!!.strokeWidth = viewSize / 10 * 3.toFloat()


        val sectorShader: Shader = SweepGradient(
            (viewSize / 2).toFloat(), (viewSize / 2).toFloat(), intArrayOf(
                Color.TRANSPARENT,
                Color.argb(
                    0,
                    Color.red(accentColor),
                    Color.green(accentColor),
                    Color.blue(accentColor)
                ),
                Color.argb(
                    255,
                    Color.red(accentColor),
                    Color.green(accentColor),
                    Color.blue(accentColor)
                )
            ), floatArrayOf(0f, 0.875f, 1f)
        )
        sectorPaint!!.shader = sectorShader
    }
}