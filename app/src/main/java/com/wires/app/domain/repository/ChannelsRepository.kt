package com.wires.app.domain.repository

import com.wires.app.data.model.Channel
import com.wires.app.managers.MockManager
import javax.inject.Inject

class ChannelsRepository @Inject constructor(
    private val mockManager: MockManager
) {
    suspend fun getChannels(): List<Channel> {
        return mockManager.getChannels()
    }
}
