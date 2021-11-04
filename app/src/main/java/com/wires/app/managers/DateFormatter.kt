package com.wires.app.managers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class DateFormatter @Inject constructor() {

    companion object {
        private const val DATE_TEXT_MONTH_TIME_TEMPLATE = "dd MMM hh:mm"
    }

    private val dateTextMonthTimeFormatter = DateTimeFormatter.ofPattern(DATE_TEXT_MONTH_TIME_TEMPLATE, Locale.getDefault())

    fun dateTimeToStringRelative(dateTime: LocalDateTime): String {
        // TODO: fix date relative
        return dateTime.format(dateTextMonthTimeFormatter)
    }
}
