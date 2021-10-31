package com.wires.app.domain.repository

import com.wires.app.data.preferences.PreferenceStorage
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) {
    fun getAccessToken() = preferenceStorage.accessToken

    fun setAccessToken(token: String) {
        preferenceStorage.accessToken = token
    }
}
