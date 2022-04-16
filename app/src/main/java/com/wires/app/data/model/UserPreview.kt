package com.wires.app.data.model

data class UserPreview(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val avatar: Image?
)
