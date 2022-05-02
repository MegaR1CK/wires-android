package com.wires.app.domain.repository

import com.google.gson.Gson
import com.wires.app.data.mapper.ChannelsMapper
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.ChannelType
import com.wires.app.data.model.Message
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.ChannelCreateParams
import com.wires.app.data.remote.params.MessageSendParams
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.data.remote.websocket.WebSocketService
import com.wires.app.extensions.toMultipartPart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ChannelsRepository @Inject constructor(
    private val apiService: WiresApiService,
    private val channelsMapper: ChannelsMapper,
    private val webSocketService: WebSocketService,
    private val gson: Gson
) {

    companion object {
        private const val IMAGE_PART_NAME = "image"
    }

    suspend fun getChannels(): List<ChannelPreview> {
        return apiService.getChannels().data.map { channelsMapper.fromResponseToModel(it) }
    }

    suspend fun getChannel(channelId: Int): Channel {
        return channelsMapper.fromResponseToModel(apiService.getChannel(channelId).data)
    }

    suspend fun getChannelMessages(channelId: Int, limit: Int, offset: Int): List<Message> {
        return apiService.getChannelMessages(channelId, limit, offset).data.map(channelsMapper::fromResponseToModel)
    }

    fun listenChannelMessages(channelId: Int): Flow<SocketEvent<Message>> {
        return webSocketService.listenChatSocket(channelId).map { it.transform(channelsMapper::fromResponseToModel) }
    }

    fun sendChannelMessage(channelId: Int, text: String) {
        webSocketService.sendChatMessage(channelId, MessageSendParams(text))
    }

    fun disconnectChannel(channelId: Int) {
        webSocketService.disconnectChatSocket(channelId)
    }

    suspend fun createChannel(name: String, type: ChannelType, membersIds: List<Int>, imagePath: String?): Channel {
        return channelsMapper.fromResponseToModel(
            apiService.createChannel(
                gson.toJson(ChannelCreateParams(name, type.name, membersIds)).toRequestBody(),
                imagePath?.let { File(it).toMultipartPart(IMAGE_PART_NAME) }
            ).data
        )
    }
}
