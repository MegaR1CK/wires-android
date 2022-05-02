package com.wires.app.data.model

import android.os.Parcel
import android.os.Parcelable
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
) : IUser, Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        username = parcel.readString().orEmpty(),
        firstName = parcel.readString(),
        lastName = parcel.readString(),
        avatar = parcel.readSerializable() as? Image,
        isSelected = parcel.readByte() != 0.toByte()
    )

    override fun getId() = id.toString()
    override fun getName() = getDisplayName()
    override fun getAvatar() = avatar?.url

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(username)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeSerializable(avatar)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserPreview> {
        override fun createFromParcel(parcel: Parcel): UserPreview {
            return UserPreview(parcel)
        }

        override fun newArray(size: Int): Array<UserPreview?> {
            return arrayOfNulls(size)
        }
    }
}
