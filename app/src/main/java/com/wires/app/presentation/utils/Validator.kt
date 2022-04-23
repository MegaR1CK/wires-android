package com.wires.app.presentation.utils

import android.util.Patterns
import com.wires.app.R
import javax.inject.Inject

class Validator @Inject constructor() {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val MAX_PASSWORD_LENGTH = 24
        private const val MAX_USERNAME_LENGTH = 24
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_NAME_LENGTH = 16
        private const val MIN_NAME_LENGTH = 2
        private const val PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$"
        private const val PATTERN_USERNAME = "^[a-zA-Z0-9_]*$"
        private const val PATTERN_NAME = "^[a-zA-Z]*$"
    }

    fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.error_empty_field
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_incorrect_email
            else -> null
        }
    }

    fun validatePassword(password: String, patternValidation: Boolean = true): Int? {
        return when {
            password.isBlank() -> R.string.error_empty_field
            password.length !in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH ->
                R.string.error_length_password.takeIf { patternValidation }
            !password.matches(PATTERN_PASSWORD.toRegex()) ->
                R.string.error_weak_password.takeIf { patternValidation }
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
            username.length !in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH ->
                R.string.error_length_username
            !username.matches(PATTERN_USERNAME.toRegex()) -> R.string.error_incorrect_username
            else -> null
        }
    }

    fun validateName(name: String): Int? {
        return when {
            name.isBlank() -> null
            name.length !in MIN_NAME_LENGTH..MAX_NAME_LENGTH ->
                R.string.error_length_name
            !name.matches(PATTERN_NAME.toRegex()) -> R.string.error_incorrect_name
            else -> null
        }
    }
}
