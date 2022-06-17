package com.wires.app.extensions

fun Int?.orDefault(default: Int = 0): Int = this ?: default
