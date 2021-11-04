package com.wires.app.presentation.views

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearSpaceItemDecoration(
    @Px private val space: Int,
    private val showFirstVerticalDivider: Boolean = false,
    private val showLastVerticalDivider: Boolean = false,
    private val showFirstHorizontalDivider: Boolean = false,
    private val showLastHorizontalDivider: Boolean = false
) : RecyclerView.ItemDecoration() {
    private var orientation = UNSET

    companion object {
        private const val UNSET = -1
        private const val VERTICAL = RecyclerView.VERTICAL
        private const val HORIZONTAL = RecyclerView.HORIZONTAL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (space != 0) {
            if (orientation == UNSET) orientation = getOrientation(parent)
            makeOffsets(outRect, view, parent, state)
        }
    }

    private fun makeOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State): Rect {
        return when (parent.layoutManager) {
            is LinearLayoutManager -> makeLinearOffsets(outRect, view, parent, state)
            else -> throw IllegalStateException("RecyclerView have unsupported layout manager")
        }
    }

    /**
     * Возвращает [Rect] c отступами для элемента линейного списка
     * По умолчанию выставляет отступ снизу
     * */
    private fun makeLinearOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State): Rect =
        outRect.apply {
            val position = parent.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION) {
                when (orientation) {
                    VERTICAL -> {
                        if (position != 0 || showFirstHorizontalDivider) top = space
                        if (position == state.itemCount - 1 && showLastHorizontalDivider) bottom = space
                    }
                    HORIZONTAL -> {
                        if (showFirstVerticalDivider) left = space
                        if (position != state.itemCount - 1 || showLastVerticalDivider) right = space
                    }
                }
            }
        }

    private fun getOrientation(parent: RecyclerView): Int {
        return if (parent.layoutManager is LinearLayoutManager) {
            (parent.layoutManager as LinearLayoutManager).orientation
        } else {
            throw IllegalStateException(
                "DividerItemDecoration can only be used with a LinearLayoutManager."
            )
        }
    }
}
