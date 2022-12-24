package com.example.otusproject_ermoshina.utill

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.ui.base.BaseDecorator

class DecoratorChildChannels(private val sizelist: Int, private val context: Context) :
    BaseDecorator(sizelist) {
    /***
     ****** This paints can draw background. You can change color
     */

    init {
        myPaintStroke.color = context.getColor(R.color.color_on_primary)
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
    companion object{
       const val MARGIN_TOP = 20
    }
}