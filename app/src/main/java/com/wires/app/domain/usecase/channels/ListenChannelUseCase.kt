package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Message
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseAsync
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseAsync<ListenChannelUseCase.Params, Flow<SocketEvent<Message>>> {
    override suspend fun execute(params: Params): Flow<SocketEvent<Message>> {
        return channelsRepository.listenChannelMessages(params.channelId)
    }

    data class Params(
        val channelId: Int
    )
}
