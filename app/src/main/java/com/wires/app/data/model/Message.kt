package com.wires.app.data.model

import java.io.Serializable
import java.time.LocalDateTime

data class Message(
    val id: Int,
    val author: UserPreview,
    val text: String,
    val sendTime: LocalDateTime,
    val isInitial: Boolean,
    val isRead: Boolean
) : Serializable
