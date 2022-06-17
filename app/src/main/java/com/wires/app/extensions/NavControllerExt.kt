package com.wires.app.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun NavController.navigateSafe(direction: NavDirections, options: NavOptions? = null) {
    currentDestination?.getAction(direction.actionId)?.let { navigate(direction, options) }
}
