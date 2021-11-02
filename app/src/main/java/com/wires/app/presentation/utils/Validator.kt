package com.wires.app.presentation.utils

import android.util.Patterns
import com.wires.app.R
import javax.inject.Inject

class Validator @Inject constructor() {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$"
        private const val PATTERN_USERNAME = "^[a-zA-Z0-9 ]*$"
    }

    fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.error_empty_field
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_incorrect_email
            else -> null
        }
    }

    fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.error_empty_field
            password.length < MIN_PASSWORD_LENGTH -> R.string.error_short_password
            !password.matches(PATTERN_PASSWORD.toRegex()) -> R.string.error_weak_password
            else -> null
        }
    }

    fun validateConfirmPasswords(password: String, confirmPassword: String): Int? {
        return when {
            confirmPassword.isBlank() -> R.string.error_empty_field
            password != confirmPassword -> R.string.error_different_passwords
            else -> null
        }
    }

    fun validateUsername(username: String): Int? {
        return when {
            username.isBlank() -> R.string.error_empty_field
            !username.matches(PATTERN_USERNAME.toRegex()) -> R.string.error_incorrect_username
            else -> null
        }
    }
}
