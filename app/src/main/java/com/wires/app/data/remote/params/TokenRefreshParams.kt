package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class TokenRefreshParams(
    @SerializedName("refresh_token")
    val refreshToken: String
)
