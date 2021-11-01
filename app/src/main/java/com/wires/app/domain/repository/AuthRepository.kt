package com.wires.app.domain.repository

import com.wires.app.managers.MockManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val mockManager: MockManager
) {
    suspend fun loginUser(email: String, passwordHash: String): String {
        return mockManager.loginUser(email, passwordHash)
    }
}
