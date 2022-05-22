package com.wires.app.managers

import com.wires.app.R
import com.wires.app.domain.repository.ResourcesRepository
import com.wires.app.extensions.capitalize
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.absoluteValue

class DateFormatter @Inject constructor(
    private val localeManager: LocaleManager,
    private val resourcesRepository: ResourcesRepository
) {

    companion object {
        private const val RELATIVE_TIME_MAX_HOURS = 4
    }

    private val dateTextMonthTimeFormatter = DateTimeFormatter.ofPattern(
        resourcesRepository.getString(R.string.template_date_text_month_time),
        localeManager.getLocale()
    )
    private val fullDateTimeFormatter = DateTimeFormatter.ofPattern(
        resourcesRepository.getString(R.string.template_full_date_time),
        localeManager.getLocale()
    )
    private val timeStandardFormatter = DateTimeFormatter.ofPattern(
        resourcesRepository.getString(R.string.template_time_standard),
        localeManager.getLocale()
    )

    private val dateDayMonthFormatter = DateTimeFormatter.ofPattern(
        resourcesRepository.getString(R.string.template_date_day_month),
        localeManager.getLocale()
    )

    private val dateStandardFormatter = DateTimeFormatter.ofPattern(
        resourcesRepository.getString(R.string.template_date_standard),
        localeManager.getLocale()
    )

    fun getDateTimeRelative(dateTime: LocalDateTime): String {
        val timeDifference = Duration.between(LocalDateTime.now(), dateTime)
        val now = LocalDateTime.now()
        return when {
            dateTime.year != now.year -> dateTime.format(fullDateTimeFormatter)
            now.dayOfYear == dateTime.dayOfYear -> {
                when {
                    timeDifference.toMinutes().absoluteValue < 1 ->
                        resourcesRepository.getString(R.string.date_time_relative_less_minute)
                    timeDifference.toHours().absoluteValue < 1 ->
                        resourcesRepository.getQuantityString(
                            R.plurals.date_time_relative_minute_ago,
                            timeDifference.toMinutes().toInt().absoluteValue,
                            timeDifference.toMinutes().toInt().absoluteValue
                        )
                    timeDifference.toHours().absoluteValue < RELATIVE_TIME_MAX_HOURS ->
                        resourcesRepository.getQuantityString(
                            R.plurals.date_time_relative_hour_ago,
                            timeDifference.toHours().toInt().absoluteValue,
                            timeDifference.toHours().toInt().absoluteValue
                        )
                    else ->
                        resourcesRepository.getString(R.string.date_time_relative_today, getTimeStandard(dateTime))
                }
            }
            now.dayOfYear - dateTime.dayOfYear == 1 ->
                resourcesRepository.getString(R.string.date_time_relative_yesterday, getTimeStandard(dateTime))
            else -> dateTime.format(dateTextMonthTimeFormatter)
        }
    }

    fun getDateRelative(date: LocalDate): String {
        val now = LocalDate.now()
        return when {
            date.dayOfYear == now.dayOfYear ->
                resourcesRepository.getString(R.string.date_relative_today).capitalize(localeManager.getLocale())
            now.dayOfYear - date.dayOfYear == 1 ->
                resourcesRepository.getString(R.string.date_relative_yesterday).capitalize(localeManager.getLocale())
            now.year == date.year -> date.format(dateDayMonthFormatter)
            else -> date.format(dateStandardFormatter)
        }
    }

    fun getTimeStandard(dateTime: LocalDateTime): String {
        return dateTime.format(timeStandardFormatter)
    }
}
