package com.wires.app.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.User
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userData = MutableLiveData<LoadableResult<User?>>()
    val userData: LiveData<LoadableResult<User?>> = _userData

    private val _createPostLiveEvent = SingleLiveEvent<Int?>()
    val createPostLiveEvent: LiveData<Int?> = _createPostLiveEvent

    fun getUser() {
        _userData.launchLoadData { userRepository.getStoredUser() }
    }

    fun openCreatePost(postId: Int? = null) {
        _createPostLiveEvent.postValue(postId)
    }
}
