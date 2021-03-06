package com.wires.app.presentation.chat

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.databinding.ItemMessageOutcomingBinding
import com.wires.app.managers.DateFormatter

class OutcomingMessageViewHolder(
    private val binding: ItemMessageOutcomingBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(messageItem: MessageListItem.ListMessage.OutcomingMessage) = with(binding) {
        val message = messageItem.message
        textViewMessageText.text = message.text
        textViewMessageTime.text = dateFormatter.getTimeStandard(message.sendTime)
    }
}
