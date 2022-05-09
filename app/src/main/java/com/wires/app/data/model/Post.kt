package com.wires.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Post(
    val id: Int,
    val author: UserPreview,
    var text: String,
    var topic: UserInterest,
    var image: Image?,
    val publishTime: LocalDateTime,
    var likesCount: Int,
    var commentsCount: Int,
    var isLiked: Boolean,
    var isEditable: Boolean = false,
    var isRemoved: Boolean = false
) : Similarable<Post>, Parcelable {

    override fun areItemsTheSame(other: Post): Boolean {
        return this.id == other.id
    }

    override fun areContentsTheSame(other: Post): Boolean {
        return this == other
    }
}
