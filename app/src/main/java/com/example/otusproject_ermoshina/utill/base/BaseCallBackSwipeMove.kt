package com.example.otusproject_ermoshina.utill.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.R


/***
 * Абстрактный класс для наследования в других классах колл-бэка свайпа
 * */
/*
------
Чтобы установить флаг для перетаскивания, установите значения flagDrag (одно или несколько):
ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
-----
Чтобы установить флаг для смахивания, установите значения flagSwipe (одно или несколько):
ItemTouchHelper.START or ItemTouchHelper.END
-----
 */

enum class Move {
    START, END, MOVED
}

typealias CallbackSwipeMove = (en: Move, position: Int, position2: Int?) -> Unit

abstract class BaseCallBackSwipeMove(private val context: Context, val callback: CallbackSwipeMove) :
    ItemTouchHelper.Callback() {
    abstract val flagDrag: Int
    abstract val flagSwipe: Int

    private var mClearPaint: Paint
    private var mBackground: ColorDrawable
    private var deleteDrawable: Drawable
    private var addDrawable: Drawable
    private var backgroundColorDelete = 0
    private var backgroundColorAdd = 0
    private var intrinsicWidth = 0
    private var intrinsicHeight = 0

    init {
        mClearPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
        mBackground = ColorDrawable()
        addDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add)!!
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!
        backgroundColorDelete = ContextCompat.getColor(context, R.color.color_primary)
        backgroundColorAdd = ContextCompat.getColor(context, R.color.color_secondary)
        intrinsicWidth = deleteDrawable.intrinsicWidth
        intrinsicHeight = deleteDrawable.intrinsicHeight
    }

    //передаю флаги разрешенного направления перетаскивания и смахивания (в обоих направлениях)
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = flagDrag
        val swipeFlags = flagSwipe
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val positionBefore = viewHolder.bindingAdapterPosition
        val positionAfter = target.bindingAdapterPosition
        callback(Move.MOVED, positionBefore, positionAfter)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //определяю позицию элемента для которого происходит сдвиг
        val position = viewHolder.bindingAdapterPosition
        when (direction) {
            ItemTouchHelper.START -> {
                callback(Move.START, position, -1)
            }
            ItemTouchHelper.END -> {
                callback(Move.END, position, -1)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView: View = viewHolder.itemView
        val itemHeight: Int = itemView.bottom - itemView.top
        val isCancelled: Boolean = dX.toInt() == 0 && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(
                c,
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        if(dY.toInt()==0){
            if (dX < 0 ) {
                mBackground.color = backgroundColorDelete
                mBackground.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                mBackground.draw(c)

                // Calculate position of delete icon
                val deleteIconTop: Int = itemView.bottom - intrinsicWidth * 2
                val deleteIconLeft: Int = itemView.right - intrinsicWidth * 2
                val deleteIconRight: Int = itemView.right
                val deleteIconBottom = deleteIconTop + intrinsicHeight * 2

                deleteDrawable.setBounds(
                    deleteIconLeft,
                    deleteIconTop,
                    deleteIconRight,
                    deleteIconBottom
                )
                deleteDrawable.draw(c)
            } else if(dX > 0){
                mBackground.color = backgroundColorAdd
                mBackground.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                mBackground.draw(c)

                // Calculate position of icon
                val addIconTop: Int = itemView.bottom - intrinsicWidth * 2
                val addIconLeft: Int = itemView.left
                val addIconRight: Int = addIconLeft + intrinsicWidth * 2
                val addIconBottom = addIconTop + intrinsicHeight * 2

                addDrawable.setBounds(
                    addIconLeft,
                    addIconTop,
                    addIconRight,
                    addIconBottom
                )
                addDrawable.draw(c)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, mClearPaint)
    }

}