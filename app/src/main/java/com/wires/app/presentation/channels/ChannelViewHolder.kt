package com.wires.app.presentation.channels

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.ChannelPreview
import com.wires.app.databinding.ItemChannelBinding
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class ChannelViewHolder(
    private val binding: ItemChannelBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channelPreview: ChannelPreview, onItemClick: (Int, Int) -> Unit) = with(binding) {
        root.setOnClickListener { onItemClick(channelPreview.id, channelPreview.unreadMessagesCount) }
        textViewChannelName.text = channelPreview.name
        channelPreview.lastSentMessage?.let { message ->
            textViewChannelLastMessage.text = message.text
            textViewChannelLastMessageDate.text = dateFormatter.getDateRelative(message.sendTime.toLocalDate())
        }
        textViewChannelUnreadMessages.text = channelPreview.unreadMessagesCount.toString()
        frameLayoutChannelBadge.isVisible = channelPreview.unreadMessagesCount > 0
        imageViewChannel.load(
            imageUrl = channelPreview.image?.url,
            placeHolderRes = R.drawable.bg_circle_placeholder,
            isCircle = true
        )
    }
}
