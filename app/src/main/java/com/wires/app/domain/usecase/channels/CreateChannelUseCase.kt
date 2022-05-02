package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelType
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Создание канала
 */
class CreateChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<CreateChannelUseCase.Params, Channel>() {

    override suspend fun execute(params: Params): Channel {
        return channelsRepository.createChannel(params.name, params.type, params.membersIds, params.imagePath)
    }

    data class Params(
        val name: String,
        val type: ChannelType,
        val membersIds: List<Int>,
        val imagePath: String?
    )
}
