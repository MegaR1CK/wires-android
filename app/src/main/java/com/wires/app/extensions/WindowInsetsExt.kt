package com.wires.app.extensions

import android.os.Build
import androidx.core.view.WindowInsetsCompat

@Suppress("deprecation")
fun WindowInsetsCompat.getKeyboardInset(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.getInsets(WindowInsetsCompat.Type.ime()).bottom
    } else {
        this.systemWindowInsetBottom
    }
}
