package com.wires.app.domain.repository

import com.wires.app.data.database.LocalStorage
import com.wires.app.data.mapper.UserMapper
import com.wires.app.data.model.User
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.data.remote.WiresApiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    preferenceStorage: PreferenceStorage,
    private val apiService: WiresApiService,
    private val localStorage: LocalStorage,
    private val userMapper: UserMapper
) {
    val isSignedIn = !preferenceStorage.accessToken.isNullOrEmpty()

    fun getStoredUser(): User? {
        return localStorage.currentUser
    }

    fun storeUser(user: User) {
        localStorage.currentUser = user
    }

    suspend fun getCurrentUser(): User {
        return userMapper.fromResponseToModel(apiService.getCurrentUser().data)
    }
}
