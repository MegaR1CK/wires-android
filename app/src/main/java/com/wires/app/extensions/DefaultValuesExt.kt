package com.wires.app.extensions

import java.time.LocalDateTime

fun Boolean?.orDefault(default: Boolean = false): Boolean = this ?: default

fun Int?.orDefault(default: Int = 0): Int = this ?: default

fun Long?.orDefault(default: Long = 0): Long = this ?: default

fun Double?.orDefault(default: Double = .0): Double = this ?: default

fun Float?.orDefault(default: Float = .0f): Float = this ?: default

fun String?.orDefault(default: String = ""): String = this ?: default

fun LocalDateTime?.orDefault(default: LocalDateTime = LocalDateTime.now()): LocalDateTime = this ?: default

fun <T> List<T>?.orDefault(default: List<T> = emptyList()): List<T> = this ?: default
