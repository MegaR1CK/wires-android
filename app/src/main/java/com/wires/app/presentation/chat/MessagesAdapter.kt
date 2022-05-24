package com.wires.app.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.data.model.Message
import com.wires.app.databinding.ItemChatDateHeaderBinding
import com.wires.app.databinding.ItemMessageIncomingBinding
import com.wires.app.databinding.ItemMessageOutcomingBinding
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

typealias IncomingMessage = MessageListItem.ListMessage.IncomingMessage
typealias OutcomingMessage = MessageListItem.ListMessage.OutcomingMessage
typealias DateHeader = MessageListItem.DateHeader

class MessagesAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
): BaseAdapter<MessageListItem, RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_OUTCOMING = 0
        private const val VIEW_TYPE_MESSAGE_INCOMING = 1
        private const val VIEW_TYPE_DATE_HEADER = 2
    }

    var senderId: Int? = null
    var isGroupMode = true

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is IncomingMessage -> VIEW_TYPE_MESSAGE_INCOMING
        is OutcomingMessage -> VIEW_TYPE_MESSAGE_OUTCOMING
        is DateHeader -> VIEW_TYPE_DATE_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MESSAGE_OUTCOMING ->
                OutcomingMessageViewHolder(
                    ItemMessageOutcomingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dateFormatter
                )
            VIEW_TYPE_MESSAGE_INCOMING ->
                IncomingMessageViewHolder(
                    ItemMessageIncomingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dateFormatter
                )
            VIEW_TYPE_DATE_HEADER ->
                DateHeaderViewHolder(
                    ItemChatDateHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    dateFormatter
                )
            else -> error("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OutcomingMessageViewHolder -> holder.bind(getItem(position) as OutcomingMessage)
            is IncomingMessageViewHolder -> holder.bind(getItem(position) as IncomingMessage)
            is DateHeaderViewHolder -> holder.bind(getItem(position) as DateHeader)
        }
    }

    fun addNewMessage(message: Message) {
        addMessageWithDateCheck(message, 0)
        notifyItemInserted(0)
    }

    fun addToEnd(messages: List<Message>) {
        val oldSize = items.size
        messages.forEach(::addMessageWithDateCheck)
        notifyItemRangeInserted(oldSize, items.size - oldSize)
    }

    private fun addMessageWithDateCheck(message: Message, index: Int? = null) {
        val messageDate = message.sendTime.toLocalDate()
        val lastItem = (items.lastOrNull() as? MessageListItem.ListMessage).takeIf { it?.message?.id != message.id }
        lastItem?.let { item ->
            val lastItemDate = item.message.sendTime.toLocalDate()
            if (lastItemDate != messageDate) items.add(DateHeader(lastItemDate))
        }
        if (index != null) items.add(index, message.toListItem()) else items.add(message.toListItem())
    }

    private fun Message.toListItem() = if (author.id == senderId) {
        OutcomingMessage(this)
    }  else {
        IncomingMessage(this, isGroupMode)
    }
}