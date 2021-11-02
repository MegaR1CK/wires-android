package com.wires.app.managers

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
}
