package com.wires.app.extensions

import android.text.Spannable
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import me.saket.bettermovementmethod.BetterLinkMovementMethod

fun TextView.enableLinks(parentView: View) {
    Linkify.addLinks(this, Linkify.ALL)
    movementMethod = BetterLinkMovementMethod.getInstance()
    setOnTouchListener { _, motionEvent ->
        val handled = movementMethod.onTouchEvent(this, text as? Spannable, motionEvent)
        performClick()
        handled || parentView.onTouchEvent(motionEvent)
    }
}
