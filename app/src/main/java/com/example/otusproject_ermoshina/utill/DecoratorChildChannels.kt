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


    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        //add padding
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = MARGIN_START_FIRST
                right = MARGIN_START
                top = MARGIN_TOP
                bottom = MARGIN_BOTTOM
            }
            else if(parent.getChildAdapterPosition(view) == sizelist-1){
                right = MARGIN_START_FIRST
                left = MARGIN_START
                top = MARGIN_TOP
                bottom = MARGIN_BOTTOM
            }else{
                right = MARGIN_START
                left = MARGIN_START
                top = MARGIN_TOP
                bottom = MARGIN_BOTTOM
            }
        }
    }
    companion object{
       const val MARGIN_TOP = 20
        const val MARGIN_START_FIRST = 1
    }
}