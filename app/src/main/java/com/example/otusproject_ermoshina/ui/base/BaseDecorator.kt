package com.example.otusproject_ermoshina.ui.base

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseDecorator(private val sizelist: Int):
    RecyclerView.ItemDecoration() {
    open val myPaintStroke: Paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
    }
    open val myPaintFill: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        //add padding
        with(outRect) {
            right = MARGIN_START
            left = MARGIN_START
            if (parent.getChildAdapterPosition(view) == 0) {
                top = MARGIN_TOP
                bottom = 0
            } else if (parent.getChildAdapterPosition(view) == sizelist - 1) {
                bottom = MARGIN_BOTTOM
                top = 0
            } else {
                bottom = MARGIN_BOTTOM
                top = 0
            }
        }
    }
    /***
     ****** add background to all elements of the RecyclerView
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        (0 until parent.childCount).map {
            val childView = parent.getChildAt(it)

            val childLeft: Int = childView.left + PADDING_DRAW
            val childTop: Int = childView.top - PADDING_DRAW
            val childRight: Int = childView.right - PADDING_DRAW
            val childBottom: Int = childView.bottom + PADDING_DRAW

            c.drawRoundRect(
                childLeft.toFloat(),
                childTop.toFloat(),
                childRight.toFloat(),
                childBottom.toFloat(),
                15f,
                15f,
                myPaintFill
            )
        }

    }
    /***
     ****** add frame to all elements of the RecyclerView
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        (0 until parent.childCount).map {
            val childView = parent.getChildAt(it)

            val childLeft: Int = childView.left + PADDING_DRAW
            val childTop: Int = childView.top - PADDING_DRAW
            val childRight: Int = childView.right - PADDING_DRAW
            val childBottom: Int = childView.bottom + PADDING_DRAW

            c.drawRoundRect(
                childLeft.toFloat(),
                childTop.toFloat(),
                childRight.toFloat(),
                childBottom.toFloat(),
                15f,
                15f,
                myPaintStroke
            )
        }
    }

    companion object {
        const val MARGIN_TOP = 8
        const val MARGIN_BOTTOM = 8
        const val MARGIN_START = 3
        const val PADDING_DRAW = 5
    }
}