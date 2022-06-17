package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Message
import com.wires.app.domain.paging.DEFAULT_LIMIT
import com.wires.app.domain.paging.DEFAULT_OFFSET
import com.wires.app.domain.repository.MessagesRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение сообщений канала по id
 */
class GetChannelMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) : UseCaseLoadable<GetChannelMessagesUseCase.Params, List<Message>>() {

    override suspend fun execute(params: Params): List<Message> {
        return messagesRepository.getChannelMessages(
            channelId = params.channelId,
            limit = params.limit ?: DEFAULT_LIMIT,
            offset = params.offset ?: DEFAULT_OFFSET
        )
    }

    data class Params(
        val channelId: Int,
        val offset: Int?,
        val limit: Int?
    )
}
