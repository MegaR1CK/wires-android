package com.wires.app.data.model

import android.os.Parcelable
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
    var isSelected: Boolean = false,
    var isEnabled: Boolean = true
) : Parcelable
