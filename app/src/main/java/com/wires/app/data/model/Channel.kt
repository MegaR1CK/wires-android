package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IDialog

data class Channel(
    val id: Int,
    val name: String,
    val image: Image?,
    val members: List<UserPreview>
) : IDialog<Message> {
    override fun getId() = id.toString()
    override fun getDialogPhoto() = image?.url
    override fun getDialogName() = name
    override fun getUsers() = members
    override fun getLastMessage() = null
    override fun getUnreadCount() = 0
    override fun setLastMessage(message: Message?) = Unit
}
