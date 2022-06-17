package com.wires.app.domain.usecase.channels

import com.wires.app.domain.repository.MessagesRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Отправка сообщения в канал по вебсокету
 */
class SendMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) : UseCaseLoadable<SendMessageUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        messagesRepository.sendChannelMessage(params.channelId, params.text, params.isInitial)
    }

    data class Params(
        val channelId: Int,
        val text: String,
        val isInitial: Boolean
    )
}
