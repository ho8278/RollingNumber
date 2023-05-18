package com.ho8278.rollingnubmer

import android.graphics.Canvas
import android.text.BoringLayout
import android.text.Layout
import android.text.TextPaint
import androidx.annotation.FloatRange
import androidx.core.graphics.withTranslation

internal class RollingColumn(private val textPaint: TextPaint) {
    private val widthMap = mutableMapOf<Char, Int>()

    private val numbers = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var mLayout: BoringLayout? = null
    private var boringLayoutMetrics: BoringLayout.Metrics? = null

    @FloatRange(from = 0.0, to = 1.0)
    var progress = 0f

    private var prevText: Char = '0'
    var text: Char = '0'
        set(value) {
            prevText = field
            if (!widthMap.contains(value)) {
                boringLayoutMetrics =
                    BoringLayout.isBoring(value.toString(), textPaint, boringLayoutMetrics)
                val width = boringLayoutMetrics?.width
                if (width != null) {
                    if (mLayout == null) {
                        mLayout = BoringLayout.make(
                            value.toString(),
                            textPaint,
                            width,
                            Layout.Alignment.ALIGN_CENTER,
                            0f,
                            0f,
                            boringLayoutMetrics,
                            true
                        )
                    }
                    widthMap[value] = width
                }
            }
            field = value
        }

    fun onDraw(canvas: Canvas) {
        if (text.isDigit() && numbers.contains(text.digitToInt())) {
            val totalRange = computeAnimateRange()
            val originalLetterTranslation = (progress * totalRange) % getHeight()
            val nextLetterTranslation = originalLetterTranslation - getHeight()
            val prevTextIndex = getValueToIndex(if (prevText.isDigit()) prevText else '0')
            val currentIdx =
                (prevTextIndex + getIndexOfYValue(progress * totalRange)) % numbers.size
            val nextIdx = (currentIdx + 1) % numbers.size
            canvas.withTranslation(y = nextLetterTranslation) {
                drawText(numbers[nextIdx].toString(), 0f, -textPaint.fontMetrics.top, textPaint)
            }
            canvas.withTranslation(y = originalLetterTranslation) {
                drawText(numbers[currentIdx].toString(), 0f, -textPaint.fontMetrics.top, textPaint)
            }
        } else {
            canvas.drawText(text.toString(), 0f, -textPaint.fontMetrics.top, textPaint)
        }
    }

    private fun computeAnimateRange(): Int {
        return if(!prevText.isDigit()) {
            getIndexDistanceToTarget(0, text.digitToInt()) * getHeight()
        } else if(!text.isDigit()) {
            0
        } else {
            getIndexDistanceToTarget(prevText.digitToInt(), text.digitToInt()) * getHeight()
        }
    }

    fun getWidth(): Int {
        return if (!widthMap.contains(text)) 0
        else widthMap[text]!!
    }

    fun getHeight(): Int {
        return mLayout?.height ?: 0
    }

    private fun getValueToIndex(value: Char): Int {
        return numbers.indexOfFirst { it.digitToChar() == value }
    }

    private fun getIndexOfYValue(yValue: Float): Int {
        return (yValue / getHeight()).toInt()
    }

    private fun getIndexDistanceToTarget(current: Int, target: Int): Int {
        val subtract = target - current
        return if (subtract > 0) subtract else (numbers.size + subtract)
    }
}