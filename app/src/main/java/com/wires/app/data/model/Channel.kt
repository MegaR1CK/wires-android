package com.wires.app.data.model

import com.stfalcon.chatkit.commons.models.IDialog
import com.wires.app.extensions.orDefault

data class Channel(
    val id: Int,
    val name: String?,
    val members: List<User>,
    val image: Image?,
    var channelLastMessage: Message?,
    val unreadMessages: Int?
) : IDialog<Message> {
    override fun getId() = id.toString()
    override fun getDialogPhoto() = image?.url
    override fun getDialogName() = name
    override fun getUsers() = members
    override fun getLastMessage() = channelLastMessage
    override fun getUnreadCount() = unreadMessages.orDefault()
    override fun setLastMessage(message: Message?) {
        channelLastMessage = message
    }
}
