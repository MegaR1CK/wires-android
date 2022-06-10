package com.wires.app.domain.repository

import com.wires.app.data.mapper.AuthMapper
import com.wires.app.data.model.TokenPair
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.data.remote.RefreshTokenApiService
import com.wires.app.data.remote.params.TokenRefreshParams
import javax.inject.Inject

class TokensRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val refreshTokenApiService: RefreshTokenApiService,
    private val authMapper: AuthMapper
) {
    fun getAccessToken() = preferenceStorage.accessToken

    fun setAccessToken(accessToken: String?) {
        preferenceStorage.accessToken = accessToken
    }

    fun getRefreshToken() = preferenceStorage.refreshToken

    fun setRefreshToken(refreshToken: String?) {
        preferenceStorage.refreshToken = refreshToken
    }

    suspend fun refreshToken(refreshToken: String): TokenPair {
        return authMapper.fromResponseToModel(
            refreshTokenApiService.refreshToken(TokenRefreshParams(refreshToken)).data
        )
    }
}
