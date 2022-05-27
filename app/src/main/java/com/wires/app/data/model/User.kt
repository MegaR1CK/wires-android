package com.wires.app.data.model

data class User(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val avatar: Image?,
    val email: String,
    val interests: List<UserInterest>
)
