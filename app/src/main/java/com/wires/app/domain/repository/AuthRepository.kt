package com.wires.app.domain.repository

import com.wires.app.data.mapper.AuthMapper
import com.wires.app.data.model.TokenPair
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.UserLoginParams
import com.wires.app.data.remote.params.UserLogoutParams
import com.wires.app.data.remote.params.UserRegisterParams
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: WiresApiService,
    private val authMapper: AuthMapper
) {
    suspend fun loginUser(email: String, passwordHash: String, deviceId: String): TokenPair {
        return authMapper.fromResponseToModel(apiService.loginUser(UserLoginParams(email, passwordHash, deviceId)).data)
    }

    suspend fun registerUser(username: String, email: String, passwordHash: String) {
        apiService.registerUser(UserRegisterParams(username, email, passwordHash))
    }

    suspend fun logoutUser(deviceId: String) {
        apiService.logoutUser(UserLogoutParams(deviceId))
    }
}
