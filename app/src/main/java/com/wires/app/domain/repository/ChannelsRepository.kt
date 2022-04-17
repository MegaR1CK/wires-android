package com.wires.app.domain.repository

import com.wires.app.data.mapper.ChannelsMapper
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.remote.WiresApiService
import javax.inject.Inject

class ChannelsRepository @Inject constructor(
    private val apiService: WiresApiService,
    private val channelsMapper: ChannelsMapper
) {
    suspend fun getChannels(): List<ChannelPreview> {
        return apiService.getChannels().data.map { channelsMapper.fromResponseToModel(it) }
    }
}
