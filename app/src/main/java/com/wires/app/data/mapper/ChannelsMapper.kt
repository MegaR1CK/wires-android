package com.wires.app.data.mapper

import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.Message
import com.wires.app.data.remote.response.ChannelPreviewResponse
import com.wires.app.data.remote.response.ChannelResponse
import com.wires.app.data.remote.response.MessageResponse
import javax.inject.Inject

class ChannelsMapper @Inject constructor(
    private val imagesMapper: ImagesMapper,
    private val userMapper: UserMapper
) {
    fun fromResponseToModel(channelPreviewResponse: ChannelPreviewResponse): ChannelPreview {
        return ChannelPreview(
            id = channelPreviewResponse.id,
            name = channelPreviewResponse.name,
            image = channelPreviewResponse.image?.let { imagesMapper.fromResponseToModel(it) },
            lastSentMessage = channelPreviewResponse.lastMessage?.let { fromResponseToModel(it) },
            unreadMessagesCount = channelPreviewResponse.unreadMessages
        )
    }

    fun fromResponseToModel(channelResponse: ChannelResponse): Channel {
        return Channel(
            id = channelResponse.id,
            name = channelResponse.name,
            image = channelResponse.image?.let { imagesMapper.fromResponseToModel(it) },
            members = channelResponse.members.map(userMapper::fromResponseToModel)
        )
    }

    fun fromResponseToModel(messageResponse: MessageResponse): Message {
        return Message(
            id = messageResponse.id,
            author = userMapper.fromResponseToModel(messageResponse.author),
            messageText = messageResponse.text,
            sendTime = messageResponse.sendTime,
            isInitial = messageResponse.isInitial,
            isRead = messageResponse.isRead
        )
    }
}
