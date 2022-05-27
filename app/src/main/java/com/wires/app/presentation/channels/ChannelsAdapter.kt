package com.wires.app.presentation.channels

import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.Message
import com.wires.app.databinding.ItemChannelBinding
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class ChannelsAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
) : BaseAdapter<ChannelPreview, ChannelViewHolder>() {

    var onItemClick: (Int, Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder(
            ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            dateFormatter
        )
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    fun sortByLastMessageDate() {
        items.sortWith(compareBy(nullsFirst()) { it.lastSentMessage?.sendTime })
        notifyItemRangeChanged(0, items.size)
    }

    fun updateLastSentMessage(channelId: Int, message: Message) {
        updateItem(channelId) { item ->
            item.lastSentMessage = message
        }
    }

    fun updateUnreadMessages(channelId: Int, unreadMessages: Int) {
        updateItem(channelId) { item ->
            item.unreadMessagesCount = unreadMessages
        }
    }

    private fun updateItem(channelId: Int, action: (ChannelPreview) -> Unit) {
        val item = items.find { it.id == channelId } ?: return
        action(item)
        notifyItemChanged(items.indexOf(item))
    }
}
