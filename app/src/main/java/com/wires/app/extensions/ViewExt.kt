package com.wires.app.extensions

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
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
 * В API 30 или некоторых девайсах fitSystemWindow=true некорректно работает для
 * элементов UI, поэтому рекомендуется использовать инсеты.
 * @param topInsetSet Добавление верхнего инсета в местах, где аппбар перекрывается статус баром.
 * @param bottomInsetSet Добавление нижнего инсета в местах, где контент уезжает под system navbar.
 * @param isKeyboardInset Используется в связке с bottomInsetSet, при открытии клавиатуры делает нижний
 * инсет равным высоте клавиатуры, из-за чего контент не перекрывается ей.
 */
fun View.setFitSystemWindowPadding(
    topInsetSet: Boolean = true,
    bottomInsetSet: Boolean = false,
    isKeyboardInset: Boolean = false,
    block: (WindowInsetsCompat) -> Unit = { }
) {
    doOnApplyWindowInsets { view, insets, _ ->
        view.updatePadding(
            top = if (topInsetSet) insets.getTopInset() else 0,
            bottom = if (bottomInsetSet && isKeyboardInset) {
                insets.getKeyboardInset().coerceAtLeast(insets.getBottomInset())
            } else if (bottomInsetSet && !isKeyboardInset) {
                insets.getBottomInset()
            } else 0
        )
        block.invoke(insets)
        insets
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
