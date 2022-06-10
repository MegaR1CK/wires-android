package com.wires.app.data.mapper

import com.wires.app.data.model.TokenPair
import com.wires.app.data.remote.response.TokenPairResponse
import javax.inject.Inject

class AuthMapper @Inject constructor() {
    fun fromResponseToModel(response: TokenPairResponse): TokenPair {
        return TokenPair(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }
}
