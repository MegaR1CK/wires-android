package com.wires.app.data.model

data class User(
    override val id: Int,
    override val username: String,
    override val firstName: String?,
    override val lastName: String?,
    override val avatar: Image?,
    val email: String,
    val interests: List<UserInterest>
) : UserPreview(id, username, firstName, lastName, avatar)
