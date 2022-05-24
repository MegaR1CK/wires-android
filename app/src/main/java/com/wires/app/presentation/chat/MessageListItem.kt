package com.wires.app.presentation.chat

import com.wires.app.data.model.Message
import java.time.LocalDate

sealed class MessageListItem {

    data class DateHeader(val date: LocalDate) : MessageListItem()

    sealed class ListMessage(open val message: Message) : MessageListItem() {

        data class OutcomingMessage(override val message: Message) : ListMessage(message)

        data class IncomingMessage(override val message: Message, val needAuthorInfo: Boolean) : ListMessage(message)
    }
}
