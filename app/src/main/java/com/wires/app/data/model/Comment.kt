package com.wires.app.data.model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val author: UserPreview,
    val text: String,
    val sendTime: LocalDateTime
) : Similarable<Comment> {

    override fun areItemsTheSame(other: Comment): Boolean {
        return this.id == other.id
    }

    override fun areContentsTheSame(other: Comment): Boolean {
        return this == other
    }
}
