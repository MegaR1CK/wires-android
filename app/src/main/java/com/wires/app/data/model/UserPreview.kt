package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IUser

data class UserPreview(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val avatar: Image?
) : IUser {
    override fun getId() = id.toString()
    override fun getName() = if (firstName != null && lastName != null) "$firstName $lastName" else username
    override fun getAvatar() = avatar?.url
}
