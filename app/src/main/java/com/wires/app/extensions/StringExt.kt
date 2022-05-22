package com.wires.app.extensions

import java.util.Locale

fun String.capitalize(locale: Locale) = replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
