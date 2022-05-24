package com.wires.app.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wires.app.data.model.Message
import com.wires.app.databinding.ItemMessageIncomingBinding
import com.wires.app.databinding.ItemMessageOutcomingBinding
import com.wires.app.managers.DateFormatter
import com.wires.app.presentation.base.BaseAdapter
import javax.inject.Inject

class MessagesAdapter @Inject constructor(
    private val dateFormatter: DateFormatter
): BaseAdapter<Message, RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_OUTCOMING = 0
        private const val VIEW_TYPE_MESSAGE_INCOMING = 1
    }

    var senderId: Int? = null
    var isGroupMode = true

    override fun getItemViewType(position: Int) =
        if (getItem(position).author.id == senderId) VIEW_TYPE_MESSAGE_OUTCOMING else VIEW_TYPE_MESSAGE_INCOMING

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
            else -> error("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OutcomingMessageViewHolder -> holder.bind(getItem(position))
            is IncomingMessageViewHolder -> holder.bind(getItem(position), isGroupMode)
        }
    }

    fun addNewMessage(message: Message) {
        items.add(0, message)
        notifyItemInserted(0)
    }

    fun addToEnd(messages: List<Message>) {
        val oldSize = items.size
        items.addAll(messages)
        notifyItemRangeInserted(oldSize, items.size - oldSize)
    }
}
