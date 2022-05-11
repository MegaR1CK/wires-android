package com.wires.app.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

private const val CLIPBOARD_LABEL = "clipboard_label"

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableRes)
}

fun Context.resolveAttribute(@AttrRes attrRes: Int): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(attrRes, outValue, true)
    return outValue.resourceId
}

fun Context.getColorAttribute(attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Context.copyToClipboard(text: String) {
    val clipboardService = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    clipboardService?.setPrimaryClip(ClipData.newPlainText(CLIPBOARD_LABEL, text))
}

fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels
