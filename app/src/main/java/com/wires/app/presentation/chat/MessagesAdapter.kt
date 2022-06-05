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
import java.time.LocalDate
import javax.inject.Inject

typealias IncomingMessage = MessageListItem.ListMessage.IncomingMessage
typealias OutcomingMessage = MessageListItem.ListMessage.OutcomingMessage
typealias DateHeader = MessageListItem.DateHeader

class MessagesAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
) : BaseAdapter<MessageListItem, RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_OUTCOMING = 0
        private const val VIEW_TYPE_MESSAGE_INCOMING = 1
        private const val VIEW_TYPE_DATE_HEADER = 2
    }

    var senderId: Int? = null
    var isGroupMode = true

    val messagesCount: Int
        get() = items.filterIsInstance<MessageListItem.ListMessage>().size

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
        val oldSize = items.size
        addMessageWithDateCheck(message, addToStart = true)
        notifyItemRangeInserted(0, items.size - oldSize)
    }

    fun addToEnd(messages: List<Message>) {
        val oldSize = items.size
        val lastHeaderDate = (items.lastOrNull() as? DateHeader)?.date
        val firstMessageDate = messages.firstOrNull()?.sendTime?.toLocalDate()
        if (lastHeaderDate == firstMessageDate && lastHeaderDate != null && firstMessageDate != null) {
            items.removeAt(items.lastIndex)
        }
        messages.forEach(::addMessageWithDateCheck)
        messages.lastOrNull()?.sendTime?.toLocalDate()?.let { date -> items.add(DateHeader(date)) }
        notifyItemRangeInserted(oldSize, items.size - oldSize)
    }

    fun getPositionById(messageId: Int): Int {
        return items.indexOf(items.find { it is MessageListItem.ListMessage && it.message.id == messageId })
    }

    private fun addMessageWithDateCheck(message: Message, addToStart: Boolean = false) {
        val messageDate = message.sendTime.toLocalDate()
        val lastItem = (if (addToStart) items.firstOrNull() else items.lastOrNull()) as? MessageListItem.ListMessage
        val lastItemWithDiffDate = lastItem.takeIf { it?.message?.id != message.id }
        if (lastItemWithDiffDate != null || (itemCount == 0 && addToStart)) {
            val lastItemDate = lastItemWithDiffDate?.message?.sendTime?.toLocalDate()
            if (lastItemDate != messageDate) {
                val dateForAdd = if (addToStart) messageDate else lastItemDate ?: LocalDate.now()
                items.add(if (addToStart) 0 else items.size, DateHeader(dateForAdd))
            }
        }
        items.add(if (addToStart) 0 else items.size, message.toListItem())
    }

    private fun Message.toListItem() = if (author.id == senderId) {
        OutcomingMessage(this)
    } else {
        IncomingMessage(this, isGroupMode)
    }
}
