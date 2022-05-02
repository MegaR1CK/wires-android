package com.wires.app.presentation.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.ChannelPreview
import com.wires.app.domain.usecase.channels.GetUserChannelsUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(
    private val getUserChannelsUseCase: GetUserChannelsUseCase
) : BaseViewModel() {

    private val _channelsLiveData = MutableLiveData<LoadableResult<List<ChannelPreview>>>()
    val channelsLiveData: LiveData<LoadableResult<List<ChannelPreview>>> = _channelsLiveData

    private val _openChatLiveEvent = SingleLiveEvent<Int>()
    val openChatLiveEvent: LiveData<Int> = _openChatLiveEvent

    private val _openCreateChannelLiveEvent = SingleLiveEvent<Unit>()
    val openCreateChannelLiveEvent: LiveData<Unit> = _openCreateChannelLiveEvent

    fun getChannels() {
        _channelsLiveData.launchLoadData(getUserChannelsUseCase.executeLoadable(Unit))
    }

    fun openChat(channelId: Int) {
        _openChatLiveEvent.postValue(channelId)
    }

    fun openCreateChannel() {
        _openCreateChannelLiveEvent.postValue(Unit)
    }
}
