package com.wires.app.domain.usecase.channels

import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Отключение от канала сообщений
 */
class DisconnectChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<DisconnectChannelUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        channelsRepository.disconnectChannel(params.channelId)
    }

    data class Params(
        val channelId: Int
    )
}
