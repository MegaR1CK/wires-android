package com.wires.app.domain.repository

import com.wires.app.data.model.Message
import com.wires.app.managers.MockManager
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val mockManager: MockManager
) {
    suspend fun getMessages(channelId: Int): List<Message> {
        return mockManager.getMessages(channelId)
    }

    suspend fun sendMessage(channelId: Int, message: Message) {
        return mockManager.sendMessage(channelId, message)
    }
}
