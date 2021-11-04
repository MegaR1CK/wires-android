package com.wires.app.managers

import com.wires.app.data.model.User
import com.wires.app.data.model.UserInterest
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockManager @Inject constructor() {

    suspend fun loginUser(email: String, passwordHash: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        delay(1000)
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    suspend fun registerUser(email: String, passwordHash: String, username: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        delay(1000)
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    suspend fun getStoredUser(): User {
        delay(1000)
        return User(
            id = 1,
            username = "kgoncharov",
            firstName = "Konstantin",
            lastName = "Goncharov",
            email = "test@test.ru",
            avatarUrl = null,
            interests = listOf(UserInterest.ANDROID_DEVELOPMENT)
        )
    }
}
