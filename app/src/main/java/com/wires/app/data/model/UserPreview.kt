package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IUser
import com.wires.app.extensions.getDisplayName

open class UserPreview(
    open val id: Int,
    open val username: String,
    open val firstName: String?,
    open val lastName: String?,
    open val avatar: Image?,
    val isSelected: Boolean = false
) : IUser {
    override fun getId() = id.toString()
    override fun getName() = getDisplayName()
    override fun getAvatar() = avatar?.url
}
