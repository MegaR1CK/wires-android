package com.wires.app.domain.repository

import com.wires.app.managers.MockManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val mockManager: MockManager
) {
    // TODO: return user from methods
    suspend fun loginUser(email: String, passwordHash: String): String {
        return mockManager.loginUser(email, passwordHash)
    }

    suspend fun registerUser(email: String, passwordHash: String, username: String): String {
        return mockManager.registerUser(email, passwordHash, username)
    }
}
