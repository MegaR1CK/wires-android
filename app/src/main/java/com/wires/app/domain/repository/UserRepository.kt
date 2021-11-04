package com.wires.app.domain.repository

import com.wires.app.data.model.User
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.managers.MockManager
import javax.inject.Inject

class UserRepository @Inject constructor(
    preferenceStorage: PreferenceStorage,
    private val mockManager: MockManager
) {
    val isSignedIn = !preferenceStorage.accessToken.isNullOrEmpty()

    suspend fun getStoredUser(): User {
        return mockManager.getStoredUser()
    }
}
