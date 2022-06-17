package com.wires.app.data.model

data class Channel(
    val id: Int,
    val name: String?,
    val type: ChannelType,
    val image: Image?,
    val members: List<UserPreview>,
    val ownerId: Int
)
