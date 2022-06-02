package com.wires.app.extensions

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import timber.log.Timber

fun NavController.navigateSafe(direction: NavDirections, options: NavOptions? = null) {
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction, options) }
}

// В блоке try/catch защита от неправильных диплинков
fun NavController.navigateSafe(uri: Uri?) {
    try {
        currentDestination?.let { uri?.let { navigate(uri) } }
    } catch (ex: Exception) {
        Timber.e(ex)
    }
}
