package com.wires.app.extensions

import androidx.core.widget.NestedScrollView

fun NestedScrollView.scrollToEnd() {
    smoothScrollTo(0, getChildAt(0).height)
}
