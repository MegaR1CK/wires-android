package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IDialog

data class ChannelPreview(
    val id: Int,
    val name: String,
    val image: Image?,
    val lastSentMessage: Message?
) : IDialog<Message> {
    override fun getId() = id.toString()
    override fun getDialogPhoto() = image?.url
    override fun getDialogName() = name
    override fun getUsers() = emptyList<UserPreview>()
    override fun getLastMessage() = lastSentMessage
    override fun getUnreadCount() = 0
    override fun setLastMessage(message: Message?) = Unit
}
