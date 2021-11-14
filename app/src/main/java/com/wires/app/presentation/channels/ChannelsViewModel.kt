package com.wires.app.presentation.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : BaseViewModel() {

    private val _channelsLiveData = MutableLiveData<LoadableResult<List<Channel>>>()
    val channelsLiveData: LiveData<LoadableResult<List<Channel>>> = _channelsLiveData

    private val _openChatLiveEvent = SingleLiveEvent<OpenChatParams>()
    val openChatLiveEvent: LiveData<OpenChatParams> = _openChatLiveEvent

    fun getChannels() {
        _channelsLiveData.launchLoadData { channelsRepository.getChannels() }
    }

    fun openChat(channelId: Int, channelName: String) {
        _openChatLiveEvent.postValue(OpenChatParams(channelId, channelName))
    }

    data class OpenChatParams(
        val channelId: Int,
        val channelName: String
    )
}
