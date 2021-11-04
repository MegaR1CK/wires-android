package com.wires.app.data.model

data class User(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val avatarUrl: String?,
    val interests: List<UserInterest>?
)
