package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IMessage
import com.wires.app.extensions.toDate
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Date

data class Message(
    val id: Int,
    val author: UserPreview,
    val messageText: String,
    val sendTime: LocalDateTime,
    val isInitial: Boolean,
    val isRead: Boolean
) : IMessage, Serializable {
    override fun getId() = id.toString()
    override fun getText() = messageText
    override fun getUser() = author
    override fun getCreatedAt(): Date = sendTime.toDate()
}
