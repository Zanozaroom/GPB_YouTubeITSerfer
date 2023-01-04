package com.example.otusproject_ermoshina.utill

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.ui.base.BaseDecorator

class DecoratorParentGrid(sizeList: Int, context: Context) :
    BaseDecorator(sizeList) {
    /***
     ****** This paints can draw background. You can change color
     */

    init {
        myPaintStroke.color = context.getColor(R.color.color_on_primary)
        myPaintFill.color = context.getColor(R.color.color_parent_decorator)
    }
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        //add padding
        with(outRect) {
            right = MARGIN_START
            left = MARGIN_START
            top = MARGIN_TOP
            bottom = MARGIN_BOTTOM

        }
    }
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
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

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

    }
    companion object{
        const val MARGIN_TOP = 20
    }

}