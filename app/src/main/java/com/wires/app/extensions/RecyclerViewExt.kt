package com.wires.app.extensions

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.presentation.views.GridSpaceItemDecoration
import com.wires.app.presentation.views.LinearSpaceItemDecoration
import com.wires.app.presentation.views.VerticalDividerItemDecoration

fun RecyclerView.addLinearSpaceItemDecoration(
    @DimenRes spacing: Int = R.dimen.padding_8,
    showFirstVerticalDivider: Boolean = false,
    showLastVerticalDivider: Boolean = false,
    showFirstHorizontalDivider: Boolean = false,
    showLastHorizontalDivider: Boolean = false
) {
    addItemDecoration(
        LinearSpaceItemDecoration(
            context.resources.getDimensionPixelSize(spacing),
            showFirstVerticalDivider,
            showLastVerticalDivider,
            showFirstHorizontalDivider,
            showLastHorizontalDivider
        )
    )
}

fun RecyclerView.addGridSpaceItemDecoration(
    @DimenRes spacing: Int = R.dimen.padding_8,
    includeEdge: Boolean = false,
    excludeTopEdge: Boolean = true
) {
    addItemDecoration(
        GridSpaceItemDecoration(
            context.resources.getDimensionPixelSize(spacing),
            includeEdge,
            excludeTopEdge
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