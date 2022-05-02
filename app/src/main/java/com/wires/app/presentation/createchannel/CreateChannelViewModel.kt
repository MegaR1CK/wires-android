package com.wires.app.presentation.createchannel

import androidx.lifecycle.LiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelType
import com.wires.app.data.model.UserPreview
import com.wires.app.domain.usecase.channels.CreateChannelUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class CreateChannelViewModel @Inject constructor(
    private val createChannelUseCase: CreateChannelUseCase
) : BaseViewModel() {

    private val _createChannelLiveEvent = SingleLiveEvent<LoadableResult<Channel>>()
    val createChannelLiveEvent: LiveData<LoadableResult<Channel>> = _createChannelLiveEvent

    private val _openPickUsersLiveEvent = SingleLiveEvent<Unit>()
    val openPickUsersLiveEvent: LiveData<Unit> = _openPickUsersLiveEvent

    var usersInChannel = listOf<UserPreview>()
    var selectedImagePath: String? = null

    fun createChannel(name: String) {
        _createChannelLiveEvent.launchLoadData(
            createChannelUseCase.executeLoadable(
                CreateChannelUseCase.Params(name, ChannelType.GROUP, usersInChannel.map(UserPreview::id), selectedImagePath)
            )
        )
    }

    fun openPickUsers() {
        _openPickUsersLiveEvent.postValue(Unit)
    }
}
