package com.wires.app.presentation.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.ChannelType
import com.wires.app.domain.usecase.channels.GetUserChannelsUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(
    private val getUserChannelsUseCase: GetUserChannelsUseCase
) : BaseViewModel() {

    private val _channelsLiveData = MutableLiveData<LoadableResult<List<ChannelPreview>>>()
    val channelsLiveData: LiveData<LoadableResult<List<ChannelPreview>>> = _channelsLiveData

    private val _openChatLiveEvent = SingleLiveEvent<OpenChatParams>()
    val openChatLiveEvent: LiveData<OpenChatParams> = _openChatLiveEvent

    private val _openCreateChannelLiveEvent = SingleLiveEvent<ChannelType>()
    val openCreateChannelLiveEvent: LiveData<ChannelType> = _openCreateChannelLiveEvent

    fun getChannels() {
        _channelsLiveData.launchLoadData(getUserChannelsUseCase.executeLoadable(Unit))
    }

    fun openChat(channelId: Int, unreadMessagesCount: Int) {
        _openChatLiveEvent.postValue(OpenChatParams(channelId, unreadMessagesCount))
    }

    fun openCreateChannel(type: ChannelType) {
        _openCreateChannelLiveEvent.postValue(type)
    }

    data class OpenChatParams(
        val channelId: Int,
        val unreadMessagesCount: Int
    )
}
