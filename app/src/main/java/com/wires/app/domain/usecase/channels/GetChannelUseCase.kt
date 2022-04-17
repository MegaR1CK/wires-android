package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Channel
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение канала по id
 */
class GetChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<GetChannelUseCase.Params, Channel>() {

    override suspend fun execute(params: Params): Channel {
        return channelsRepository.getChannel(params.channelId)
    }

    data class Params(
        val channelId: Int
    )
}
