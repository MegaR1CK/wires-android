package com.wires.app.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Message
import com.wires.app.data.model.User
import com.wires.app.domain.repository.MessagesRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import java.time.LocalDateTime
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _messagesLiveData = MutableLiveData<LoadableResult<List<Message>>>()
    val messagesLiveData: LiveData<LoadableResult<List<Message>>> = _messagesLiveData

    private val _userLiveData = MutableLiveData<LoadableResult<User>>()
    val userLiveData: LiveData<LoadableResult<User>> = _userLiveData

    private val _sendMessageLiveEvent = SingleLiveEvent<LoadableResult<Unit>>()
    val sendMessageLiveEvent: LiveData<LoadableResult<Unit>> = _sendMessageLiveEvent

    private val _displayMessageLiveEvent = SingleLiveEvent<Message>()
    val displayMessageLiveEvent: LiveData<Message> = _displayMessageLiveEvent

    fun getMessages(channelId: Int) {
        _messagesLiveData.launchLoadData { messagesRepository.getMessages(channelId) }
    }

    fun getUser() {
        _userLiveData.launchLoadData { userRepository.getStoredUser() }
    }

    fun sendMessage(channelId: Int, text: String) {
        _userLiveData.value?.getOrNull()?.let { user ->
            val message = Message(
                id = -1,
                author = user,
                sendTime = LocalDateTime.now(),
                messageText = text,
                isUnread = false
            )
            _sendMessageLiveEvent.launchLoadData { messagesRepository.sendMessage(channelId, message) }
            _displayMessageLiveEvent.postValue(message)
        }
    }
}
