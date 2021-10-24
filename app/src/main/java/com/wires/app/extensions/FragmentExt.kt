package com.wires.app.extensions

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wires.app.R

const val TOOLBAR_COLLAPSED_TAG = "collapsed"
const val TOOLBAR_EXPANDED_TAG = "expanded"

fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

fun Fragment.hideSoftKeyboard(view: View) {
    activity?.hideSoftKeyboard(view)
}

fun Fragment.showSoftKeyboard(showFlags: Int = InputMethodManager.SHOW_FORCED, hideFlags: Int = 0) {
    activity?.showSoftKeyboard(showFlags, hideFlags)
}

fun Fragment.showSoftKeyboard(view: View, showFlags: Int = 0) {
    activity?.showSoftKeyboard(view, showFlags)
}

fun Fragment.isKeyboardVisible(insets: WindowInsetsCompat): Boolean {
    return activity?.isKeyboardVisible(insets) ?: false
}

fun Fragment.errorSnackbar(
    message: String,
    actionText: String? = null,
    action: () -> Unit
) {
    showErrorSnackbar(message, actionText, action = action)
}

fun Fragment.showToast(message: String) {
    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
    }
}

private fun Fragment.showErrorSnackbar(
    message: String,
    actionText: String? = null,
    action: () -> Unit
) {
    view?.let {
        Snackbar
            .make(it, message, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                action.invoke()

            }.show()
    }
}

fun Fragment.share(shareMessage: String) {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
    shareIntent.type = "text/plain"
    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(Intent.createChooser(shareIntent, context?.getString(R.string.share)))
}

fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigateSafe(direction)
}

fun Fragment.navigateTo(uri: Uri?) {
    findNavController().navigateSafe(uri)
}
