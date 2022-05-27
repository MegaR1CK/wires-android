package com.wires.app.data.model

data class Channel(
    val id: Int,
    val name: String,
    val image: Image?,
    val members: List<UserPreview>
)
