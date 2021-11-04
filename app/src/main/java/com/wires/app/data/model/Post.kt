package com.wires.app.data.model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    val author: User,
    val publishTime: LocalDateTime,
    val text: String,
    val imageUrl: String?,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean
)
