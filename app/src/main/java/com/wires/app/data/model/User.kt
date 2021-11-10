package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IUser

data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatarUrl: String?,
    val interests: List<UserInterest>?
) : IUser {
    override fun getId() = id.toString()
    override fun getName() = "$firstName $lastName"
    override fun getAvatar() = avatarUrl
}
