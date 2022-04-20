package com.wires.app.domain.usecase.channels

import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Отправка сообщения в канал по вебсокету
 */
class SendMessageUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<SendMessageUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        channelsRepository.sendChannelMessage(params.channelId, params.text)
    }

    data class Params(
        val channelId: Int,
        val text: String
    )
}
