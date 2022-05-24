package com.wires.app.presentation.chat

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.Message
import com.wires.app.databinding.ItemMessageIncomingBinding
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class IncomingMessageViewHolder(
    private val binding: ItemMessageIncomingBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message, isGroupMode: Boolean) = with(binding) {
        imageViewMessageAuthorAvatar.isVisible = isGroupMode
        textViewMessageAuthorName.isVisible = isGroupMode
        imageViewMessageAuthorAvatar.load(
            imageUrl = message.author.avatar?.url,
            placeHolderRes = R.drawable.ic_avatar_placeholder,
            isCircle = true
        )
        textViewMessageAuthorName.text = message.author.getDisplayName()
        textViewMessageText.text = message.messageText
        textViewMessageTime.text = dateFormatter.getTimeStandard(message.sendTime)
    }
}
