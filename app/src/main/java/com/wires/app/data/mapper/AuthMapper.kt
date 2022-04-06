package com.wires.app.data.mapper

import com.wires.app.data.model.Token
import com.wires.app.data.remote.response.TokenResponse
import javax.inject.Inject

class AuthMapper @Inject constructor() {
    fun fromResponseToModel(response: TokenResponse): Token {
        return Token(token = response.token)
    }
}
