package com.wires.app.domain.repository

import com.google.gson.Gson
import com.wires.app.data.mapper.ChannelsMapper
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.ChannelType
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.ChannelCreateParams
import com.wires.app.data.remote.params.ChannelEditParams
import com.wires.app.data.remote.websocket.WebSocketService
import com.wires.app.extensions.toMultipartPart
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

    fun disconnectChannel(channelId: Int) {
        webSocketService.disconnectChatSocket(channelId)
    }

    suspend fun createChannel(name: String?, type: ChannelType, membersIds: List<Int>, imagePath: String?): Channel {
        return channelsMapper.fromResponseToModel(
            apiService.createChannel(
                createParams = gson.toJson(ChannelCreateParams(name, type.name, membersIds)).toRequestBody(),
                image = imagePath?.let { File(it).toMultipartPart(IMAGE_PART_NAME) }
            ).data
        )
    }

    suspend fun editChannel(channelId: Int, name: String, membersIds: List<Int>, imagePath: String?) {
        apiService.editChannel(
            channelId = channelId,
            updateParams = gson.toJson(ChannelEditParams(name, membersIds)).toRequestBody(),
            image = imagePath?.let { File(it).toMultipartPart(IMAGE_PART_NAME) }
        )
    }
}
