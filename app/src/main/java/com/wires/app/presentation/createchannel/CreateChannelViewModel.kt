package com.wires.app.presentation.createchannel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelType
import com.wires.app.data.model.UserPreview
import com.wires.app.domain.usecase.channels.CreateChannelUseCase
import com.wires.app.domain.usecase.channels.EditChannelUseCase
import com.wires.app.domain.usecase.channels.GetChannelUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class CreateChannelViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase,
    private val getChannelUseCase: GetChannelUseCase,
    private val editChannelUseCase: EditChannelUseCase
) : BaseViewModel() {

    private val _channelLiveData = MutableLiveData<LoadableResult<Channel>>()
    val channelLiveData: LiveData<LoadableResult<Channel>> = _channelLiveData

    private val _createChannelLiveEvent = SingleLiveEvent<LoadableResult<Channel>>()
    val createChannelLiveEvent: LiveData<LoadableResult<Channel>> = _createChannelLiveEvent

    private val _editChannelLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val editChannelLiveEvent: LiveData<LoadableResult<Unit>> = _editChannelLiveEvent

    private val _openPickUsersLiveEvent = SingleLiveEvent<Unit>()
    val openPickUsersLiveEvent: LiveData<Unit> = _openPickUsersLiveEvent

    var usersInChannel = listOf<UserPreview>()
    var selectedImagePath: String? = null

    fun getChannel(channelId: Int) {
        _channelLiveData.launchLoadData(getChannelUseCase.executeLoadable(GetChannelUseCase.Params(channelId)))
    }

    fun createChannel(name: String) {
        _createChannelLiveEvent.launchLoadData(
            createChannelUseCase.executeLoadable(
                CreateChannelUseCase.Params(name, ChannelType.GROUP, usersInChannel.map(UserPreview::id), selectedImagePath)
            )
        )
    }

    fun editChannel(channelId: Int, name: String) {
        _editChannelLiveEvent.launchLoadData(
            editChannelUseCase.executeLoadable(
                EditChannelUseCase.Params(channelId, name, usersInChannel.map(UserPreview::id), selectedImagePath)
            )
        )
    }

    fun openPickUsers() {
        _openPickUsersLiveEvent.postValue(Unit)
    }
}
