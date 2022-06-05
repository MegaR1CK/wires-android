package com.wires.app.data.model

data class ChannelPreview(
    val id: Int,
    val name: String?,
    val type: ChannelType,
    val image: Image?,
    var lastSentMessage: Message?,
    var unreadMessagesCount: Int,
    val dialogMember: UserPreview?
)
