package com.wires.app.presentation.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.domain.repository.ChannelsRepository
import com.wires.app.presentation.base.BaseViewModel
import javax.inject.Inject

class ChannelsViewModel @Inject constructor(
    private val channelsRepository: ChannelsRepository
) : BaseViewModel() {

    private val _channelsLiveData = MutableLiveData<LoadableResult<List<Channel>>>()
    val channelsLiveData: LiveData<LoadableResult<List<Channel>>> = _channelsLiveData

    fun getChannels() {
        _channelsLiveData.launchLoadData { channelsRepository.getChannels() }
    }
}
