package com.wires.app.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat

fun Activity.hideSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showSoftKeyboard(showFlags: Int = InputMethodManager.SHOW_FORCED, hideFlags: Int = 0) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(showFlags, hideFlags)
}

fun Activity.showSoftKeyboard(view: View, showFlags: Int = 0) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, showFlags)
}

fun Activity.isKeyboardVisible(insets: WindowInsetsCompat): Boolean {
    val keyboardRatio = 0.25
    val systemScreenHeight = this.window.decorView.height
    val heightDiff = insets.getKeyboardInset() + insets.getTopInset()
    return heightDiff > keyboardRatio * systemScreenHeight
}

fun Activity.restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val mainIntent = Intent.makeRestartActivityTask(intent?.component)
    finish()
    startActivity(mainIntent)
}
