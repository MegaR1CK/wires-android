package com.wires.app.managers

import com.wires.app.R
import com.wires.app.domain.repository.ResourcesRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class DateFormatter @Inject constructor(
    private val resourcesRepository: ResourcesRepository
) {

    companion object {
        private const val DATE_TEXT_MONTH_TIME_TEMPLATE = "dd MMMM 'at' hh:mm"
        private const val FULL_DATE_TIME_TEMPLATE = "dd MMMM yyyy 'at' hh:mm"
        private const val TIME_STANDARD_TEMPLATE = "hh:mm"
        private const val RELATIVE_TIME_MAX_HOURS = 4
    }

    private val dateTextMonthTimeFormatter = DateTimeFormatter.ofPattern(DATE_TEXT_MONTH_TIME_TEMPLATE, Locale.getDefault())
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern(FULL_DATE_TIME_TEMPLATE, Locale.getDefault())
    private val timeStandardFormatter = DateTimeFormatter.ofPattern(TIME_STANDARD_TEMPLATE, Locale.getDefault())

    fun dateTimeToStringRelative(dateTime: LocalDateTime): String {
        val timeDifference = Duration.between(LocalDateTime.now(), dateTime)
        val now = LocalDateTime.now()
        return when {
            dateTime.year != now.year -> dateTime.format(fullDateTimeFormatter)
            now.dayOfYear == dateTime.dayOfYear -> {
                when {
                    timeDifference.toMinutes().absoluteValue < 1 ->
                        resourcesRepository.getString(R.string.date_relative_less_minute)
                    timeDifference.toHours().absoluteValue < 1 ->
                        resourcesRepository.getQuantityString(
                            R.plurals.date_relative_minute_ago,
                            timeDifference.toMinutes().toInt().absoluteValue,
                            timeDifference.toMinutes().toInt().absoluteValue
                        )
                    timeDifference.toHours().absoluteValue < RELATIVE_TIME_MAX_HOURS ->
                        resourcesRepository.getQuantityString(
                            R.plurals.date_relative_hour_ago,
                            timeDifference.toHours().toInt().absoluteValue,
                            timeDifference.toHours().toInt().absoluteValue
                        )
                    else ->
                        resourcesRepository.getString(R.string.date_relative_today, getTimeStandard(dateTime))
                }
            }
            now.dayOfYear - dateTime.dayOfYear == 1 ->
                resourcesRepository.getString(R.string.date_relative_yesterday, getTimeStandard(dateTime))
            else -> dateTime.format(dateTextMonthTimeFormatter)
        }
    }

    fun getTimeStandard(dateTime: LocalDateTime): String {
        return dateTime.format(timeStandardFormatter)
    }
}
