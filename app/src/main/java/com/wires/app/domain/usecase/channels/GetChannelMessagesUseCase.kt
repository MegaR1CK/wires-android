package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Message
import com.wires.app.domain.paging.DEFAULT_LIMIT
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение сообщений канала по id
 */
class GetChannelMessagesUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<GetChannelMessagesUseCase.Params, List<Message>>() {

    override suspend fun execute(params: Params): List<Message> {
        return channelsRepository.getChannelMessages(params.channelId, DEFAULT_LIMIT, params.offset)
    }

    data class Params(
        val channelId: Int,
        val offset: Int
    )
}
