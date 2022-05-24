package com.wires.app.presentation.chat

import androidx.recyclerview.widget.RecyclerView
import com.wires.app.databinding.ItemChatDateHeaderBinding
import com.wires.app.managers.DateFormatter

class DateHeaderViewHolder(
    private val binding: ItemChatDateHeaderBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dateItem: MessageListItem.DateHeader) {
        binding.textViewHeader.text = dateFormatter.getDateRelative(dateItem.date)
    }
}
