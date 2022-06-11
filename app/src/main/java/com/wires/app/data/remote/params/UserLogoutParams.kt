package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class UserLogoutParams(
    @SerializedName("refresh_token")
    val refreshToken: String
)
