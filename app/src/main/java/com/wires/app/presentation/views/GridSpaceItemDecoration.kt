package com.wires.app.presentation.views

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(
    @Px private val spacing: Int,
    private val includeEdge: Boolean,
    private val excludeTopEdge: Boolean
) : RecyclerView.ItemDecoration() {

    private var orientation = -1

    companion object {
        private const val VERTICAL = RecyclerView.VERTICAL
        private const val HORIZONTAL = RecyclerView.HORIZONTAL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager is GridLayoutManager) {
            val grid = parent.layoutManager as GridLayoutManager
            val spanCount = grid.spanCount

            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (orientation == -1) orientation = getOrientation(parent)

            if (includeEdge) {
                if (orientation == VERTICAL) {
                    outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                } else {
                    if (column == 0) {
                        if (!excludeTopEdge) {
                            outRect.top =
                                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                        }
                    } else {
                        outRect.top = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    }
                }
                if (orientation == VERTICAL) {
                    outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                } else {
                    outRect.bottom = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                }

                if (position < spanCount) { // topWindowInsets edge
                    if (orientation == VERTICAL) {
                        if (!excludeTopEdge) {
                            outRect.top = spacing
                        }
                    } else {
                        outRect.left = spacing
                    }
                }
                // item bottomWindowInsets
                if (orientation == VERTICAL) {
                    outRect.bottom = spacing
                } else {
                    outRect.right = spacing
                }
            } else {
                if (orientation == VERTICAL) {
                    outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                } else {
                    outRect.top = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                }
                if (orientation == VERTICAL) {
                    outRect.right = spacing - (column + 1) * spacing / spanCount
                } else {
                    outRect.bottom = spacing - (column + 1) * spacing / spanCount
                }
                if (position >= spanCount) {
                    // item topWindowInsets
                    if (orientation == VERTICAL) {
                        outRect.top = spacing
                    } else {
                        outRect.left = spacing
                    }
                }
            }
        } else {
            throw IllegalStateException(
                "GridSpacingItemDecoration can only be used with a GridLayoutManager."
            )
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        return if (parent.layoutManager is GridLayoutManager) {
            (parent.layoutManager as GridLayoutManager).orientation
        } else {
            throw IllegalStateException(
                "GridSpacingItemDecoration can only be used with a GridLayoutManager."
            )
        }
    }
}
