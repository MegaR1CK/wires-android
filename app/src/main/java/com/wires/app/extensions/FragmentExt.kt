package com.wires.app.extensions

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wires.app.presentation.views.LoadableResultDialog

fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

fun Fragment.showSnackbar(
    message: String,
    actionText: String? = null,
    action: () -> Unit = { }
) {
    view?.let {
        Snackbar
            .make(it, message, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                action.invoke()
            }.show()
    }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigateSafe(direction)
}

fun Fragment.navigateBack() = findNavController().popBackStack()

fun Fragment.createLoadableResultDialog(message: String? = null): LoadableResultDialog {
    return LoadableResultDialog(requireContext(), message)
}

fun Fragment.showAlertDialog(
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    @StringRes positiveButtonTextRes: Int,
    @StringRes negativeButtonTextRes: Int,
    positiveButtonListener: DialogInterface.OnClickListener
) = AlertDialog.Builder(requireContext())
    .setTitle(getString(titleRes))
    .setMessage(getString(messageRes))
    .setPositiveButton(getText(positiveButtonTextRes), positiveButtonListener)
    .setNegativeButton(getText(negativeButtonTextRes)) { _, _ -> }
    .create()
    .show()
