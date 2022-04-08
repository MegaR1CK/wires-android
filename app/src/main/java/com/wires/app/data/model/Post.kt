package com.wires.app.data.model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    override val author: UserPreview,
    override val text: String,
    val topic: String,
    val imageUrl: String?,
    val publishTime: LocalDateTime,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean,
) : BasePost(author, text), Similarable<Post> {

    override fun areItemsTheSame(other: Post): Boolean {
        return this.id == other.id
    }

    override fun areContentsTheSame(other: Post): Boolean {
        return this == other
    }
}
