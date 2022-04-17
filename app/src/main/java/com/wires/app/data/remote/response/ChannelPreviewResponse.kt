package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChannelPreviewResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("last_message")
    val lastMessage: MessageResponse?
)
