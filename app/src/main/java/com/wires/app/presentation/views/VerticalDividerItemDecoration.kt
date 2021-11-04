package com.wires.app.presentation.views

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Рисует разделитель после элемента
 * @param drawable - разделитель, который нужно рисовать
 * @param firstDividerPosition - начиная с какого элемента нужно рисовать разделители
 * @param shouldDrawFirstDivider - нужно ли рисовать первый разделитель (сверху первого элемента)
 * @param endOffset - количество элементов с конца, у которых не нужно рисовать разделитель
 * -- По умолчанию endOffset = 1, в этом случае так как разделитель рисуется снизу, он будет только между элементами
 * -- При endOffset = 0 будет нарисован разделитель за последним элементом,
 * но в таком случае у recyclerView paddingBottom должен быть > 0
 */
class VerticalDividerItemDecoration(
    private var drawable: Drawable,
    private val firstDividerPosition: Int = 0,
    private val shouldDrawFirstDivider: Boolean = false,
    private val endOffset: Int = 1
) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.layoutManager?.childCount ?: parent.childCount

        for (i in 0..childCount - (endOffset + 1)) {
            val child = parent.getChildAt(i)

            if (parent.getChildAdapterPosition(child) >= firstDividerPosition) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                if (shouldDrawFirstDivider && parent.getChildAdapterPosition(child) == firstDividerPosition) {
                    val dividerTop = child.top
                    val dividerBottom = dividerTop + drawable.intrinsicHeight
                    drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    drawable.draw(canvas)
                }
                val dividerTop = child.bottom + params.bottomMargin
                val dividerBottom = dividerTop + drawable.intrinsicHeight
                drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                drawable.draw(canvas)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapterPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        if (adapterPosition <= firstDividerPosition || adapterPosition >= itemCount - if (endOffset == 0) 0 else 1) {
            return
        }

        outRect.top = drawable.intrinsicHeight
    }
}
