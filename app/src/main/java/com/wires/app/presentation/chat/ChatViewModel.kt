package com.wires.app.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.Message
import com.wires.app.data.model.UserWrapper
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.domain.usecase.channels.DisconnectChannelUseCase
import com.wires.app.domain.usecase.channels.GetChannelMessagesUseCase
import com.wires.app.domain.usecase.channels.GetChannelUseCase
import com.wires.app.domain.usecase.channels.ListenChannelUseCase
import com.wires.app.domain.usecase.channels.ReadMessagesUseCase
import com.wires.app.domain.usecase.channels.SendMessageUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val getChannelUseCase: GetChannelUseCase,
    private val getChannelMessagesUseCase: GetChannelMessagesUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val listenChannelUseCase: ListenChannelUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val readMessagesUseCase: ReadMessagesUseCase,
    private val disconnectChannelUseCase: DisconnectChannelUseCase
) : BaseViewModel() {

    private val _channelLiveData = MutableLiveData<LoadableResult<Channel>>()
    val channelLiveData: LiveData<LoadableResult<Channel>> = _channelLiveData

    private val _messagesLiveData = MutableLiveData<LoadableResult<List<Message>>>()
    val messagesLiveData: LiveData<LoadableResult<List<Message>>> = _messagesLiveData

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _sendMessageLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val sendMessageLiveEvent: LiveData<LoadableResult<Unit>> = _sendMessageLiveEvent

    private val _receiveMessageLiveEvent = SingleLiveEvent<SocketEvent<Message>>()
    val receiveMessageLiveEvent: LiveData<SocketEvent<Message>> = _receiveMessageLiveEvent

    val messagesIdsForRead = mutableSetOf<Int>()

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun getChannel(channelId: Int) {
        _channelLiveData.launchLoadData(getChannelUseCase.executeLoadable(GetChannelUseCase.Params(channelId)))
    }

    fun getMessages(channelId: Int, offset: Int? = null, limit: Int? = null) {
        _messagesLiveData.launchLoadData(
            getChannelMessagesUseCase.executeLoadable(GetChannelMessagesUseCase.Params(channelId, offset, limit))
        )
    }

    fun listenChannel(channelId: Int) {
        _receiveMessageLiveEvent.launchSocketData(listenChannelUseCase.execute(ListenChannelUseCase.Params(channelId)))
    }

    fun sendMessage(channelId: Int, text: String, isInitial: Boolean) {
        _sendMessageLiveEvent.launchLoadData(
            sendMessageUseCase.executeLoadable(SendMessageUseCase.Params(channelId, text, isInitial))
        )
    }

    fun readMessages(channelId: Int) = if (messagesIdsForRead.isNotEmpty()) {
        readMessagesUseCase.executeOutOfLifecycle(ReadMessagesUseCase.Params(channelId, messagesIdsForRead)) { result ->
            result.doOnFailure { error -> Timber.e(error.message) }
        }
    } else Unit

    fun disconnectChannel(channelId: Int) =
        disconnectChannelUseCase.executeOutOfLifecycle(DisconnectChannelUseCase.Params(channelId)) { result ->
            result.doOnFailure { error -> Timber.e(error.message) }
        }
}
