package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenPairResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
