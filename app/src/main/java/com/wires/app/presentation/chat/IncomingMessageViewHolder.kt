package com.wires.app.presentation.chat

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.databinding.ItemMessageIncomingBinding
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class IncomingMessageViewHolder(
    private val binding: ItemMessageIncomingBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(messageItem: MessageListItem.ListMessage.IncomingMessage) = with(binding) {
        val message = messageItem.message
        val needAuthorInfo = messageItem.needAuthorInfo
        imageViewMessageAuthorAvatar.isVisible = needAuthorInfo
        textViewMessageAuthorName.isVisible = needAuthorInfo
        imageViewMessageAuthorAvatar.load(
            imageUrl = message.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewMessageAuthorName.text = message.author.getDisplayName()
        textViewMessageText.text = message.text
        textViewMessageTime.text = dateFormatter.getTimeStandard(message.sendTime)
    }
}
