package com.wires.app.extensions

import com.wires.app.data.model.User
import com.wires.app.data.model.UserPreview

fun UserPreview.getDisplayName() = when {
    firstName == null -> username
    lastName == null -> firstName
    else -> "$firstName $lastName"
}

fun User.getDisplayName() = when {
    firstName == null -> username
    lastName == null -> firstName
    else -> "$firstName $lastName"
}
