package com.example.otusproject_ermoshina.utill

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.otusproject_ermoshina.utill.base.BaseCallBackSwipeMove
import com.example.otusproject_ermoshina.utill.base.CallbackSwipeMove

class SwipeToFavoriteList (context: Context, swipeDelete: CallbackSwipeMove) :
    BaseCallBackSwipeMove(context,swipeDelete) {
    override val flagDrag = 0
    override val flagSwipe = ItemTouchHelper.END
}