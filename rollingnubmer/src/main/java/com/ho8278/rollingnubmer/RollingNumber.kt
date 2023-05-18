package com.ho8278.rollingnubmer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class RollingNumber @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.rollingNumberStyle
) : View(context, attrs, defStyleAttr) {

    private val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val rollingColumnManager = RollingColumnManager(textPaint)

    private var prevText = ""
    var text = ""
        set(value) {
            rollingColumnManager.setText(value)
            startRollingAnimation()
            prevText = field
            field = value
            checkRequestLayout()
        }

    private var animator: Animator? = null
    private var duration = DEFAULT_DURATION

    private var textColorStateList: ColorStateList? = null

    init {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val obtainStyleable =
            context.obtainStyledAttributes(attrs, R.styleable.RollingNumber, defStyleAttr, 0)
        for (i in 0 until obtainStyleable.indexCount) {
            when (val styleable = obtainStyleable.getIndex(i)) {
                R.styleable.RollingNumber_android_text -> {
                    text = obtainStyleable.getString(styleable).orEmpty()
                }
                R.styleable.RollingNumber_android_textSize -> {
                    val size = obtainStyleable.getDimension(styleable, DEFAULT_TEXT_SIZE)
                    setTextSize(size)
                }
                R.styleable.RollingNumber_android_textColor -> {
                    val colorStateList = obtainStyleable.getColorStateList(styleable)
                    if (colorStateList != null) setTextColor(colorStateList)
                }
                R.styleable.RollingNumber_android_textStyle -> {
                    val textStyle = obtainStyleable.getInt(styleable, Typeface.NORMAL)
                    setTextStyle(textStyle)
                }
                R.styleable.RollingNumber_android_duration -> {
                    val duration = obtainStyleable.getInt(styleable, DEFAULT_DURATION)
                    setDuration(duration)
                }
            }
        }
        obtainStyleable.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { rollingColumnManager.onDraw(it) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(rollingColumnManager.getWidth(), rollingColumnManager.getHeight())
    }

    fun setTextStyle(style: Int) {
        val typeface = Typeface.defaultFromStyle(style)
        setTypeface(typeface)
    }

    fun setTypeface(typeface: Typeface) {
        textPaint.typeface = typeface
    }

    fun setTextSize(size: Float) {
        textPaint.textSize = size
    }

    fun setTextColor(colorStateList: ColorStateList) {
        textColorStateList = colorStateList
        updateTextColorInternal()
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    private fun updateTextColorInternal() {
        textPaint.color = textColorStateList?.getColorForState(drawableState, 0) ?: 0
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateTextColorInternal()
    }

    private fun startRollingAnimation() {
        animator?.end()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = this@RollingNumber.duration.toLong()
            addUpdateListener {
                rollingColumnManager.setProgress(it.animatedFraction)
                invalidate()
            }
        }
        animator?.start()
    }

    private fun checkRequestLayout() {
        if (prevText == text) return
        if (prevText.length == text.length) return
        else requestLayout()
    }

    companion object {
        private const val DEFAULT_TEXT_SIZE = 16f
        private const val DEFAULT_DURATION = 1000

    }
}