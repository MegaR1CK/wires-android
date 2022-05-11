package com.wires.app.managers

import android.app.Activity
import com.wires.app.extensions.restartApp
import com.yariksoffice.lingver.Lingver
import java.util.Locale
import javax.inject.Inject

class LocaleManager @Inject constructor() {

    companion object {
        private const val LOCALE_ENGLISH = "en"
        private const val LOCALE_ENGLISH_COUNTRY = "EN"
        private const val LOCALE_RUSSIAN = "ru"
        private const val LOCALE_RUSSIAN_COUNTRY = "RU"
    }

    private val lingver = Lingver.getInstance()

    fun setLocale(activity: Activity, locale: AvailableLocale) {
        val newLocale = Locale(locale.language, locale.country)
        if (lingver.getLocale().language != newLocale.language) {
            lingver.setLocale(activity, newLocale)
            activity.restartApp()
        }
    }
    enum class AvailableLocale(val language: String, val country: String) {
        RUSSIAN(LOCALE_RUSSIAN, LOCALE_RUSSIAN_COUNTRY),
        ENGLISH(LOCALE_ENGLISH, LOCALE_ENGLISH_COUNTRY)
    }
}
