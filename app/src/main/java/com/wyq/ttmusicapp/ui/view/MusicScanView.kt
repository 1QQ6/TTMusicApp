package com.wyq.ttmusicapp.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.utils.DisplayUtil
import java.util.*
import kotlin.math.max
import kotlin.math.min

private val paint1: Paint
    get() {
        val paint = Paint()
        return paint
    }

/**
 * Created by Roman on 2021/1/30
 */
class MusicScanView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    /**
     * 默认主题颜色
     */
    private val DEFAULT_COLOR = Color.parseColor("#FFFFFFFF")

    /**
     * 主题色
     */
    private var accentColor:Int? = null
    /**
     *圆环的各层颜色
     */
    private var colorList = mutableListOf<String>()
    /**
     * 圆圈的数量
     */
    private var mCircleNum = 4
    /**
     * 扫描的颜色
     */
    private val mSweepColor = DEFAULT_COLOR
    /**
     * 是否显示交叉线
     */
    private var isShowCross = true
    /**
     * 扫描的速度
     */
    private var mSpeed = 1.0f
    /**
     *圆的画笔
     */
    private var mCirclePaint: Paint? = null
    /**
     * 扫描效果的画笔
     */
    private var mSweepPaint:Paint? = null

    /**
     * 扫描运动偏移量参数
     */
    private var mOffsetArgs = 0.0f

    /**
     * 是否扫描
     */
    private var isScanning = false

    private var drawCircle = false

    /**
     *中间的图片
     */
    private var bitmap:Bitmap? = null

    private var bitmapPaint:Paint? = null

    /**
     * 是否显示水滴
     */
    private var isShowRainDrop = true

    /**
     * 水滴的颜色
     */
    private var rainDropColor = Color.parseColor("#ff0000")

    /**
     * 水滴的数量
     */
    private var rainDropCount = 4

    /**
     *水滴的画笔
     */
    private var mRaindropPaint : Paint? = null

    /**
     * 保存水滴数据
     */
    private val mRainDropsList = ArrayList<Raindrop>()

    /**
     * 水滴显示和消失的速度
     */
    private var mFlicker: Float = 3.0f


    init {
        initCircleColor(attrs)
        mCirclePaint = Paint()
        mCirclePaint?.style = Paint.Style.FILL
        mCirclePaint?.isAntiAlias = true

        bitmapPaint = Paint()

        mSweepPaint = Paint()
        mSweepPaint?.isAntiAlias = true
        initBitmap()
    }

    private fun initBitmap() {
        val tintDrawable = tintDrawable(
            ContextCompat.getDrawable(context, R.drawable.music_note),
            ColorStateList.valueOf(accentColor!!)
        )
        bitmap = drawableToBitmap(tintDrawable)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w-30, h-30)
        drawable.draw(canvas)
        return bitmap
    }

    private fun tintDrawable(drawable: Drawable?, colors: ColorStateList?): Drawable{
        //潜在地包装，以便可以通过此类中的着色方法在不同的API级别上进行着色。
        val wrap = DrawableCompat.wrap(drawable!!)
        //指定drawable的颜色作为颜色状态列表。
        DrawableCompat.setTintList(wrap,colors)
        return wrap
    }

    /**
     * 初始化各层圆的色值
     */
    private fun initCircleColor(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.theme)
        accentColor = typedArray.getColor(
            R.styleable.theme_accent_color,
            ContextCompat.getColor(context, R.color.colorDarkBackground)
        )
        typedArray.recycle()
        val color =
            colorToHexString(Color.red(accentColor!!)) + colorToHexString(
                Color.green(accentColor!!)
            ) + colorToHexString(Color.blue(accentColor!!))

        colorList.add("#30$color")
        colorList.add("#50$color")
        colorList.add("#70$color")
        colorList.add("#D0$color")
    }

    companion object {
        fun colorToHexString(color: Int): String {
            return if (color < 16) {
                //toHexString:返回整数参数的字符串表示形式，为以16为底的无符号整数
                "0" + Integer.toHexString(color)
            } else {
                Integer.toHexString(color)
            }
        }

        fun changeAlpha(mSweepColor: Int, alpha: Int): Int {
            val red = Color.red(mSweepColor)
            val green = Color.green(mSweepColor)
            val blue = Color.blue(mSweepColor)
            return Color.argb(alpha, red, green, blue)
        }
    }

    /**
     * 开始扫描
     */
    fun start() {
        if (!isScanning) {
            isScanning = true
            invalidate()
        }
    }

    /**
     * 停止扫描
     */
    fun stop() {
        if (isScanning) {
            isScanning = false
            //mRaindrops.clear()
            mOffsetArgs = 0.0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置宽高，默认两百dp
        val defaultSize = DisplayUtil.dip2px(context,200f)
        setMeasuredDimension(
            measureWidth(widthMeasureSpec,defaultSize),
            measureHeight(widthMeasureSpec,defaultSize))
    }

    /**
     * 测量高
     */
    private fun measureHeight(heightMeasureSpec: Int, defaultSize: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(heightMeasureSpec)
        val specSize = MeasureSpec.getSize(defaultSize)
        //MeasureSpec.EXACTLY:父级已确定子级的确切大小。
        // 不管孩子想要多大，都会给孩子以这些界限。
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize
        }else{
            result = defaultSize + paddingTop + paddingBottom
            //子级可以根据需要的大小而定，最大可以达到指定的大小specSize。
            if (specMode == MeasureSpec.AT_MOST){
                result = min(result,specSize)
            }
        }
        result = max(result,suggestedMinimumHeight)
        return result
    }

    /**
     * 测量宽
     */
    private fun measureWidth(widthMeasureSpec: Int, defaultSize: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(defaultSize)
        //MeasureSpec.EXACTLY:父级已确定子级的确切大小。
        // 不管孩子想要多大，都会给孩子以这些界限。
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize
        }else{
            result = defaultSize + paddingLeft + paddingRight
            //子级可以根据需要的大小而定，最大可以达到指定的大小specSize。
            if (specMode == MeasureSpec.AT_MOST){
                result = min(result,specSize)
            }
        }
        result = max(result,suggestedMinimumWidth)
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //计算圆的半径
        val circleWidth = width - paddingLeft - paddingRight
        val circleHeight = height - paddingTop - paddingBottom
        val circleRadius = min(circleWidth,circleHeight)/2
        //计算圆心
        // circleX = 半径 + 左内边距
        val circleX = paddingLeft + circleWidth / 2
        // circleY = 半径 + 上内边距
        val circleY = paddingTop + circleHeight / 2
        //开始画圆
        drawCircle(canvas,circleX,circleY,circleRadius)
        //如果正在扫描，则绘制扫描动画
        if(isScanning){
            drawCircleSweep(canvas,circleX,circleY,circleRadius)
            //计算雷达扫描的旋转角度
            mOffsetArgs = (mOffsetArgs + 360 / mSpeed / 60) % 360
            //请求重新draw()，但只会绘制调用者本身
            invalidate()
        }
    }

    /**
     * 画扫描效果
     * SweepGradient
     * 第一个参数：cx表示圆心x轴坐标
     * 第二个参数：cy表示圆心y轴坐标
     * 第三个参数：colors[]表示渐变色数组
     * 第四个参数：positions[]和颜色数组对应，表示每种颜色在渐变方向上所占的百分比，取值[0, 1]
     */
    private fun drawCircleSweep(canvas: Canvas?, circleX: Int, circleY: Int, circleRadius: Int) {
        val sweepColorArray = intArrayOf(
            Color.TRANSPARENT,
            changeAlpha(mSweepColor, 0),
            changeAlpha(mSweepColor, 88),
            changeAlpha(mSweepColor, 166),
            changeAlpha(mSweepColor, 255)
        )
        val positionArray = floatArrayOf(0.0f, 0.33f, 0.66f, 0.88f, 1f)

        val sweepGradient = SweepGradient(
            circleX.toFloat(),
            circleY.toFloat(),
            sweepColorArray,
            positionArray
        )
        mSweepPaint?.shader = sweepGradient
        //先旋转画布，再绘制扫描的颜色渲染，实现扫描时的旋转效果。
        canvas!!.rotate(mOffsetArgs, circleX.toFloat(), circleY.toFloat())
        canvas.drawCircle(circleX.toFloat(), circleX.toFloat(), circleRadius.toFloat(), mSweepPaint!!)
    }

    /**
     * @param circleX 圆心的横坐标
     * @param circleY 圆心的纵坐标
     * @param circleRadius 圆的半径
     */
    private fun drawCircle(canvas: Canvas?, circleX: Int, circleY: Int, circleRadius: Int) {
        bitmapPaint?.isAntiAlias = true
        //Rect():用指定的坐标创建一个新的矩形。
        // 注意：不执行范围检查，因此调用者必须确保左<=右和上<=下
        val rectSrc = Rect(0,0,bitmap!!.width,bitmap!!.height)
        //src是对bitmap裁剪
        //如果是整张，那么：
        //Rect src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight())
        //dst是将图片绘制到View的哪个位置
        val itemWidth = circleRadius / mCircleNum
        val rectDst = Rect(circleX-itemWidth+20,circleY-itemWidth,circleX+itemWidth,circleY+itemWidth)
        canvas?.drawBitmap(bitmap!!,rectSrc,rectDst,
            bitmapPaint!!)

        for (i in 0 until mCircleNum){
            mCirclePaint?.color = Color.parseColor(colorList[i])
            val radius = circleRadius - (circleRadius / mCircleNum * i)
            canvas?.drawCircle(
                circleX.toFloat(),
                circleY.toFloat(),
                radius.toFloat(),
                mCirclePaint!!
            )
        }
    }

    /**
     * 水滴数据类
     */
    private class Raindrop(var x: Int, var y: Int, var radius: Float, var color: Int) {
        var alpha = 255f

        /**
         * 获取改变透明度后的颜色值
         *
         * @return
         */
        fun changeAlpha(): Int {
            return changeAlpha(color, alpha.toInt())
        }

    }
}