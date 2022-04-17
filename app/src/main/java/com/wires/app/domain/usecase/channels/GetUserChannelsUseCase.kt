package com.wires.app.domain.usecase.channels

import com.wires.app.data.model.ChannelPreview
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение каналов пользователя с сервера
 */
class GetUserChannelsUseCase @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : UseCaseLoadable<Unit, List<ChannelPreview>>() {

    override suspend fun execute(params: Unit): List<ChannelPreview> {
        return channelsRepository.getChannels()
    }
}
