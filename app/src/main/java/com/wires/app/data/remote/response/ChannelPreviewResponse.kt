package com.wires.app.data.remote.response

import com.google.gson.annotations.SerializedName
import com.wires.app.data.model.ChannelType

data class ChannelPreviewResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: ChannelType,
    @SerializedName("image")
    val image: ImageResponse?,
    @SerializedName("last_message")
    val lastMessage: MessageResponse?,
    @SerializedName("unread_messages")
    val unreadMessages: Int,
    @SerializedName("dialog_member")
    val dialogMember: UserPreviewResponse?
)
