package com.wires.app.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Post
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.posts.GetUserPostsUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.domain.usecase.user.GetUserByIdUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : BaseViewModel() {

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _userPostsLiveData = MutableLiveData<PagingData<Post>>()
    val userPostsLiveData: LiveData<PagingData<Post>> = _userPostsLiveData

    private val _userPostsStateLiveData = MutableLiveData<LoadableResult<Unit>>()
    val userPostsStateLiveData: LiveData<LoadableResult<Unit>> = _userPostsStateLiveData

    private val _openPostLiveEvent = SingleLiveEvent<Int>()
    val openPostLiveEvent: LiveData<Int> = _openPostLiveEvent

    private val _openProfileLiveEvent = SingleLiveEvent<Int>()
    val openProfileLiveEvent: LiveData<Int> = _openProfileLiveEvent

    private val _openEditUserLiveEvent = SingleLiveEvent<Unit>()
    val openEditUserLiveEvent: LiveData<Unit> = _openEditUserLiveEvent

    private val _currentUserLiveData = MutableLiveData<LoadableResult<UserWrapper>>()

    val isCurrentUserProfile: Boolean
        get() {
            return _currentUserLiveData.value?.getOrNull() == _userLiveData.value?.getOrNull()
        }

    fun getUser(userId: Int) {
        val currentUserFlow = getStoredUserUseCase.executeLoadable(Unit)
        _currentUserLiveData.launchLoadData(currentUserFlow)
        if (userId == 0) {
            _userLiveData.launchLoadData(currentUserFlow)
        } else {
            _userLiveData.launchLoadData(getUserByIdUseCase.executeLoadable(GetUserByIdUseCase.Params(userId)))
        }
    }

    fun getUserPosts(userId: Int) {
        _userPostsLiveData.launchPagingData(getUserPostsUseCase.execute(GetUserPostsUseCase.Params(userId)))
    }

    fun bindLoadingState(states: CombinedLoadStates) {
        _userPostsStateLiveData.bindPagingState(states)
    }

    fun openPost(postId: Int) {
        _openPostLiveEvent.postValue(postId)
    }

    fun openProfile(userId: Int) {
        _openProfileLiveEvent.postValue(userId)
    }

    fun openEditUser() {
        _openEditUserLiveEvent.postValue(Unit)
    }
}
