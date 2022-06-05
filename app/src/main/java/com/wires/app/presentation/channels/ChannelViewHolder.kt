package com.wires.app.presentation.channels

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.R
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.ChannelType
import com.wires.app.databinding.ItemChannelBinding
import com.wires.app.extensions.getDisplayName
import com.wires.app.extensions.load
import com.wires.app.managers.DateFormatter

class ChannelViewHolder(
    private val binding: ItemChannelBinding,
    private val dateFormatter: DateFormatter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channelPreview: ChannelPreview, onItemClick: (Int, Int) -> Unit) = with(binding) {
        root.setOnClickListener { onItemClick(channelPreview.id, channelPreview.unreadMessagesCount) }
        if (channelPreview.type == ChannelType.GROUP) {
            textViewChannelName.text = channelPreview.name
            imageViewChannel.load(
                imageUrl = channelPreview.image?.url,
                placeHolderRes = R.drawable.bg_circle_placeholder,
                isCircle = true
            )
        } else {
            textViewChannelName.text = channelPreview.dialogMember?.getDisplayName()
            imageViewChannel.load(
                imageUrl = channelPreview.dialogMember?.avatar?.url,
                placeHolderRes = R.drawable.ic_avatar_placeholder,
                isCircle = true
            )
        }
        channelPreview.lastSentMessage?.let { message ->
            textViewChannelLastMessage.text = message.text
            textViewChannelLastMessageDate.text = dateFormatter.getDateRelative(message.sendTime.toLocalDate())
        }
        textViewChannelUnreadMessages.text = channelPreview.unreadMessagesCount.toString()
        frameLayoutChannelBadge.isVisible = channelPreview.unreadMessagesCount > 0
    }
}
