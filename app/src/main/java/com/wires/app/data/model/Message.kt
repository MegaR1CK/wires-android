package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IMessage
import com.wires.app.extensions.toDate
import java.time.LocalDateTime
import java.util.Date

data class Message(
    val id: Int,
    val author: User,
    val sendTime: LocalDateTime,
    val messageText: String,
    val isUnread: Boolean
) : IMessage {
    override fun getId() = id.toString()
    override fun getText() = messageText
    override fun getUser() = author
    override fun getCreatedAt(): Date = sendTime.toDate()
}
