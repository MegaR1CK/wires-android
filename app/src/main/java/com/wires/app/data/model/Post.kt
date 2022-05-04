package com.wires.app.data.model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    val author: UserPreview,
    val text: String,
    val topic: UserInterest,
    val image: Image?,
    val publishTime: LocalDateTime,
    var likesCount: Int,
    var commentsCount: Int,
    var isLiked: Boolean,
    var isEditable: Boolean = false
) : Similarable<Post> {

    override fun areItemsTheSame(other: Post): Boolean {
        return this.id == other.id
    }

    override fun areContentsTheSame(other: Post): Boolean {
        return this == other
    }
}
