package com.wires.app.domain.usecase.channels

import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Чтение сообщений в канале
 */
class ReadMessagesUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<ReadMessagesUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        channelsRepository.readChannelMessages(params.channelId, params.messagesIds)
    }

    data class Params(
        val channelId: Int,
        val messagesIds: Set<Int>
    )
}
