package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("interests")
    val interests: List<String>,
    @SerializedName("avatar")
    val avatar: ImageResponse?,
)
