package com.wires.app.domain.usecase.channels

import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Редактирование канала
 */
class EditChannelUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<EditChannelUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        channelsRepository.editChannel(params.channelId, params.name, params.membersIds, params.imagePath)
    }

    data class Params(
        val channelId: Int,
        val name: String,
        val membersIds: List<Int>,
        val imagePath: String?
    )
}
