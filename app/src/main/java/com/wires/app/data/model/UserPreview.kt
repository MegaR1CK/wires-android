package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IUser
import com.wires.app.extensions.getDisplayName

/**
 * Не используем наследуемость UserPreview -> User, так как от UserPreview требуется поведение Data класса
 */
data class UserPreview(
    val id: Int,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val avatar: Image?,
    var isSelected: Boolean = false
) : IUser {
    override fun getId() = id.toString()
    override fun getName() = getDisplayName()
    override fun getAvatar() = avatar?.url
}
