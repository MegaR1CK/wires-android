package com.wires.app.extensions

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.wires.app.R
import com.wires.app.presentation.views.FlexboxSpaceItemDecoration
import com.wires.app.presentation.views.LinearSpaceItemDecoration
import com.wires.app.presentation.views.VerticalDividerItemDecoration

fun RecyclerView.addLinearSpaceItemDecoration(
    @DimenRes spacing: Int = R.dimen.padding_8,
    showFirstVerticalDivider: Boolean = false,
    showLastVerticalDivider: Boolean = false,
    showFirstHorizontalDivider: Boolean = false,
    showLastHorizontalDivider: Boolean = false,
    shouldDecorate: (parent: RecyclerView, child: View) -> Boolean = { _, _ -> true }
) {
    addItemDecoration(
        LinearSpaceItemDecoration(
            context.resources.getDimensionPixelSize(spacing),
            showFirstVerticalDivider,
            showLastVerticalDivider,
            showFirstHorizontalDivider,
            showLastHorizontalDivider,
            shouldDecorate
        )
    )
}

fun RecyclerView.addVerticalDividerItemDecoration(
    @DrawableRes drawableRes: Int = R.drawable.recycler_view_divider,
    firstDividerPosition: Int = 0,
    shouldDrawFirstDivider: Boolean = false,
    endOffset: Int = 1
) {
    context.getDrawableCompat(drawableRes)?.let {
        setPadding(
            paddingLeft,
            if (shouldDrawFirstDivider) paddingTop + it.intrinsicHeight else paddingTop,
            paddingRight,
            if (endOffset == 0) paddingBottom + it.intrinsicHeight else paddingBottom
        )
        addItemDecoration(
            VerticalDividerItemDecoration(
                it,
                firstDividerPosition,
                shouldDrawFirstDivider,
                endOffset
            )
        )
    }
}

fun RecyclerView.addFlexboxSpaceItemDecoration(@DimenRes spacing: Int) {
    val itemSpacing = resources.getDimensionPixelSize(spacing)
    addItemDecoration(FlexboxSpaceItemDecoration(itemSpacing, itemSpacing))
}

fun RecyclerView.setupScrollWithAppBar(appBarLayout: AppBarLayout, rootView: View) = post {
    val appBarBehavior = ((appBarLayout.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior as? AppBarLayout.Behavior)
    val childrenHeight = children.sumOf { it.height }
    val isAppBarScrollable = childrenHeight > (rootView.measuredHeight - appBarLayout.measuredHeight) && adapter?.itemCount != 0
    isNestedScrollingEnabled = isAppBarScrollable
    appBarBehavior?.setDragCallback(
        object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return isAppBarScrollable
            }
        }
    )
}
