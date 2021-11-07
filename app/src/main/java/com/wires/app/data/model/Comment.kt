package com.wires.app.data.model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val author: User,
    val publishTime: LocalDateTime,
    val text: String
)
