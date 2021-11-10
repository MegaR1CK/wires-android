package com.wires.app.data.model

import java.time.LocalDateTime
// TODO: get comments in post
data class Post(
    override val author: User,
    override val text: String,
    val publishTime: LocalDateTime,
    val id: Int,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean,
    val imageUrl: String?
) : BasePost(author, text)
