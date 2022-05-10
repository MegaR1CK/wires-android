package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserPreviewResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar")
    val avatar: ImageResponse?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?
)
