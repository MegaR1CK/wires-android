package com.wires.app.data.model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    val author: UserPreview,
    val text: String,
    val topic: String,
    val image: Image?,
    val publishTime: LocalDateTime,
    var likesCount: Int,
    val commentsCount: Int,
    var isLiked: Boolean,
) : Similarable<Post> {

    override fun areItemsTheSame(other: Post): Boolean {
        return this.id == other.id
    }

    override fun areContentsTheSame(other: Post): Boolean {
        return this == other
    }
}
