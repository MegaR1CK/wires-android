package com.wires.app.presentation.chat

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.data.model.Message
import com.wires.app.databinding.ItemMessageOutcomingBinding
import com.wires.app.managers.DateFormatter

class OutcomingMessageViewHolder(
    private val binding: ItemMessageOutcomingBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) = with(binding) {
        textViewMessageText.text = message.messageText
        textViewMessageTime.text = dateFormatter.getTimeStandard(message.sendTime)
    }
}
