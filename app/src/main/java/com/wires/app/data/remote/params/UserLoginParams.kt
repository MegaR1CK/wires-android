package com.wires.app.data.remote.params

import com.google.gson.annotations.SerializedName

data class UserLoginParams(
    @SerializedName("email")
    val email: String,
    @SerializedName("password_hash")
    val passwordHash: String
)
