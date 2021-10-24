package com.wires.app.extensions

import android.os.Build
import androidx.core.view.WindowInsetsCompat

fun WindowInsetsCompat.getTopInset(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.getInsets(WindowInsetsCompat.Type.systemBars()).top
    } else {
        this.systemWindowInsetTop
    }
}

fun WindowInsetsCompat.getKeyboardInset(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.getInsets(WindowInsetsCompat.Type.ime()).bottom
    } else {
        this.systemWindowInsetBottom
    }
}

/** Нижний отступ без учета клавиатуры */
fun WindowInsetsCompat.getBottomInset(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
    } else {
        0
    }
}
