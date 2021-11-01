package com.wires.app.presentation.utils

import android.util.Patterns
import com.wires.app.R
import javax.inject.Inject

class Validator @Inject constructor() {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$"
    }

    fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.error_empty_email
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_incorrect_email
            else -> null
        }
    }

    fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.error_empty_password
            password.length < MIN_PASSWORD_LENGTH -> R.string.error_short_password
            !password.matches(PATTERN_PASSWORD.toRegex()) -> R.string.error_weak_password
            else -> null
        }
    }
}
