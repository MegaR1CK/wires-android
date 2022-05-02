package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IUser
import com.wires.app.extensions.getDisplayName

data class User(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val avatar: Image?,
    val email: String,
    val interests: List<UserInterest>
) : IUser {
    override fun getId() = id.toString()
    override fun getName() = getDisplayName()
    override fun getAvatar() = avatar?.url
}
