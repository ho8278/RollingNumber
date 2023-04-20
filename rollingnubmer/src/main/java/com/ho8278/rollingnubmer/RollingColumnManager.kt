package com.ho8278.rollingnubmer

import android.graphics.Canvas
import android.text.TextPaint

internal class RollingColumnManager(private val textPaint: TextPaint) {
    private val columnList = mutableListOf<RollingColumn>()

    fun setText(text: String) {
        columnList.clear()
        text.forEach {
            val column = RollingColumn(textPaint)
            column.text = it
            columnList.add(column)
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