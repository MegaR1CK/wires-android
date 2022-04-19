package com.wires.app.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.Message
import com.wires.app.data.model.UserWrapper
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.domain.usecase.channels.GetChannelMessagesUseCase
import com.wires.app.domain.usecase.channels.GetChannelUseCase
import com.wires.app.domain.usecase.channels.ListenChannelUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val getChannelUseCase: GetChannelUseCase,
    private val getChannelMessagesUseCase: GetChannelMessagesUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val listenChannelUseCase: ListenChannelUseCase,
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

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun getChannel(channelId: Int) {
        _channelLiveData.launchLoadData(getChannelUseCase.executeLoadable(GetChannelUseCase.Params(channelId)))
    }

    fun getMessages(channelId: Int, offset: Int) {
        _messagesLiveData.launchLoadData(
            getChannelMessagesUseCase.executeLoadable(GetChannelMessagesUseCase.Params(channelId, offset))
        )
    }

    fun listenChannel(channelId: Int) = viewModelScope.launch {
        _receiveMessageLiveEvent.launchSocketData(listenChannelUseCase.execute(ListenChannelUseCase.Params(channelId)))
    }

    fun sendMessage(channelId: Int, text: String) {
//        _userLiveData.value?.getOrNull()?.let { user ->
//            val message = Message(
//                id = -1,
//                author = user,
//                sendTime = LocalDateTime.now(),
//                messageText = text,
//                isUnread = false
//            )
//            _sendMessageLiveEvent.launchLoadData { messagesRepository.sendMessage(channelId, message) }
//            _displayMessageLiveEvent.postValue(message)
//        }
    }
}
