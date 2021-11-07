package com.wires.app.extensions

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            }
        )
    }
}

/**
 * Метод выставляет у вью паддинг, равный высоте статус бара (верхнему системному инсету).
 * При этом помечает, что обработал top inset
 * */
fun View.fitTopInsetsWithPadding(callback: (WindowInsetsCompat) -> Unit = {}) {
    this.doOnApplyWindowInsets { view, insets, paddings ->
        val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            top = windowInsets.top + paddings.top
        )
        callback.invoke(insets)

        WindowInsetsCompat.Builder().setInsets(
            WindowInsetsCompat.Type.systemBars(),
            Insets.of(
                windowInsets.left,
                0,
                windowInsets.right,
                windowInsets.bottom
            )
        ).build()
    }
}

/**
 * Метод выставляет у вью паддинг, равный высоте статус бара (верхнему системному инсету).
 * Метод выставляет у вью паддинг, равный высоте панели навигации (нижнему системному инсету).
 * При этом помечает, что обработал top и bottom insets
 * */
fun View.fitTopAndBottomInsetsWithPadding(callback: (WindowInsetsCompat) -> Unit = {}) {
    this.doOnApplyWindowInsets { view, insets, paddings ->
        val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.navigationBars())
        view.updatePadding(
            top = windowInsets.top + paddings.top,
            bottom = windowInsets.bottom + paddings.bottom
        )
        callback.invoke(insets)

        WindowInsetsCompat.Builder().setInsets(
            WindowInsetsCompat.Type.systemBars(),
            Insets.of(
                windowInsets.left,
                0,
                windowInsets.right,
                0
            )
        ).build()
    }
}

/**
 * Метод выставляет у вью паддинг сверху, равный высоте статус бара (верхнему системному инсету),
 * и паддинг снизу, равный высоте клавиатуры.
 * При этом помечает, что обработал top и bottom insets
 */
fun View.fitKeyboardInsetsWithPadding(block: (View, WindowInsetsCompat, Rect) -> Unit = { _, _, _ -> }) {
    this.doOnApplyWindowInsets { view, insets, paddings ->
        val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
        view.updatePadding(
            top = windowInsets.top + paddings.top,
            bottom = windowInsets.bottom + paddings.bottom
        )
        block.invoke(view, insets, paddings)
        WindowInsetsCompat.Builder().setInsets(
            WindowInsetsCompat.Type.systemBars(),
            Insets.of(
                windowInsets.left,
                0,
                windowInsets.right,
                0
            )
        ).build()
    }
}

fun View.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    start?.let { params?.marginStart = it }
    top?.let { params?.topMargin = it }
    end?.let { params?.marginEnd = it }
    bottom?.let { params?.bottomMargin = it }
    layoutParams = params
}
