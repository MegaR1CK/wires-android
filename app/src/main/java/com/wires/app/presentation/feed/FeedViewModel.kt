package com.wires.app.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.User
import com.wires.app.domain.repository.UserRepository
import com.wires.app.presentation.base.BaseViewModel
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userData = MutableLiveData<LoadableResult<User>>()
    val userData: LiveData<LoadableResult<User>> = _userData

    fun getUser() {
        _userData.launchLoadData { userRepository.getStoredUser() }
    }
}
