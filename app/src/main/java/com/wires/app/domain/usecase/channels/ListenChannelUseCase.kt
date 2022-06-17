package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Message
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.domain.repository.MessagesRepository
import com.wires.app.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenChannelUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) : UseCase<ListenChannelUseCase.Params, Flow<SocketEvent<Message>>> {

    override fun execute(params: Params): Flow<SocketEvent<Message>> {
        return messagesRepository.listenChannelMessages(params.channelId)
    }

    data class Params(
        val channelId: Int
    )
}
