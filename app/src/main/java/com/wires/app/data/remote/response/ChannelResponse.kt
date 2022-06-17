package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName
import com.wires.app.data.model.ChannelType

data class ChannelResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: ChannelType,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("members")
    val members: List<UserPreviewResponse>,
    @SerializedName("owner_id")
    val ownerId: Int
)
