package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChannelResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("members")
    val members: List<UserPreviewResponse>
)
