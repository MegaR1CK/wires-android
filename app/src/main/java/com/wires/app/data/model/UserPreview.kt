package com.wires.app.data.model

import android.os.Parcelable
import com.stfalcon.chatkit.commons.models.IUser
import com.wires.app.extensions.getDisplayName
import kotlinx.parcelize.Parcelize

/**
 * Не используем наследуемость UserPreview -> User, так как от UserPreview требуется поведение Data класса
 */
@Parcelize
data class UserPreview(
    val id: Int,
    val username: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: Image?,
    var isSelected: Boolean = false
) : IUser, Parcelable {
    override fun getId() = id.toString()
    override fun getName() = getDisplayName()
    override fun getAvatar() = avatar?.url
}
