package com.ho8278.rollingnubmer

import android.graphics.Canvas
import android.text.TextPaint

internal class RollingColumnManager(private val textPaint: TextPaint) {
    private val columnList = mutableListOf<RollingColumn>()

    fun setText(text: String) {
        val count = text.length - columnList.size
        if (count > 0) {
            repeat(count) {
                columnList.add(RollingColumn(textPaint))
            }
        } else if (count < 0) {
            repeat(-count) {
                columnList.removeFirst()
            }
        }

        for (i in 0..text.lastIndex) {
            columnList[i].text = text[i]
        }
    }

    fun getText(): String {
        val builder = StringBuilder()
        columnList.forEach { builder.append(it.text) }
        return builder.toString()
    }

    fun onDraw(canvas: Canvas) {
        columnList.forEach {
            it.onDraw(canvas)
            canvas.translate(it.getWidth().toFloat(), 0f)
        }
    }

    fun getWidth(): Int {
        return columnList.fold(0) { acc, value -> acc + value.getWidth() }
    }

    fun getHeight(): Int {
        return if (columnList.isEmpty()) 0 else columnList.maxOf { it.getHeight() }
    }

    fun setTextSize(textSize: Float) {
        textPaint.textSize = textSize
    }

    fun setProgress(progress: Float) {
        columnList.forEach {
            it.progress = progress
        }
    }
}