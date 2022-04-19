package com.wires.app.domain.repository

import com.google.gson.Gson
import com.wires.app.data.mapper.ChannelsMapper
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.Message
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.websocket.ChatWebSocketFactory
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.extensions.toSocketEventFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChannelsRepository @Inject constructor(
    private val apiService: WiresApiService,
    private val channelsMapper: ChannelsMapper,
    private val chatWebSocketFactory: ChatWebSocketFactory,
    private val gson: Gson
) {

    suspend fun getChannels(): List<ChannelPreview> {
        return apiService.getChannels().data.map { channelsMapper.fromResponseToModel(it) }
    }

    suspend fun getChannel(channelId: Int): Channel {
        return channelsMapper.fromResponseToModel(apiService.getChannel(channelId).data)
    }

    suspend fun getChannelMessages(channelId: Int, limit: Int, offset: Int): List<Message> {
        return apiService.getChannelMessages(channelId, limit, offset).data.map(channelsMapper::fromResponseToModel)
    }

    suspend fun listenChannelMessages(channelId: Int): Flow<SocketEvent<Message>> {
        return chatWebSocketFactory.create(channelId).apply {
            withContext(Dispatchers.IO) { connect() }
        }.toSocketEventFlow(gson, channelsMapper::fromResponseToModel)
    }
//
//    fun sendChannelMessage(channelId: Int, text: String): Boolean {
//        return chatServiceFactory
//            .create("channel/$channelId")
//            .sendMessage(text)
//    }
}
