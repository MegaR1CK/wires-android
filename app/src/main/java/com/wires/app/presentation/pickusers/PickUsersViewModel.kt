package com.wires.app.presentation.pickusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.ChannelType
import com.wires.app.data.model.UserPreview
import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.usecase.channels.CreateChannelUseCase
import com.wires.app.domain.usecase.user.GetStoredUserUseCase
import com.wires.app.domain.usecase.user.SearchUsersUseCase
import com.wires.app.extensions.addOrRemove
import com.wires.app.extensions.mapResultValue
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class PickUsersViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val createChannelUseCase: CreateChannelUseCase
) : BaseViewModel() {

    companion object {
        private const val MIN_QUERY_LENGTH = 3
    }

    private val _searchResultLiveData = MutableLiveData<LoadableResult<List<UserPreview>>>()
    val searchResultLiveData: LiveData<LoadableResult<List<UserPreview>>> = _searchResultLiveData

    private val _addedUsersLiveData = MutableLiveData<List<UserPreview>>()
    val addedUsersLiveData: LiveData<List<UserPreview>> = _addedUsersLiveData

    private val _searchErrorLiveEvent = SingleLiveEvent<Unit>()
    val searchErrorLiveEvent: LiveData<Unit> = _searchErrorLiveEvent

    private val _userLiveData = MutableLiveData<LoadableResult<UserWrapper>>()
    val userLiveData: LiveData<LoadableResult<UserWrapper>> = _userLiveData

    private val _createChannelLiveEvent = SingleLiveEvent<LoadableResult<Channel>>()
    val createChannelLiveEvent: LiveData<LoadableResult<Channel>> = _createChannelLiveEvent

    var pickedUsers = mutableListOf<UserPreview>()
        set(value) {
            field = value
            _addedUsersLiveData.postValue(value)
        }

    var lastSearchQuery: String? = null

    fun search(query: String? = lastSearchQuery) = validateSearchQuery(query)?.let { validationResult ->
        lastSearchQuery = validationResult
        _searchResultLiveData.launchLoadData(
            searchUsersUseCase.executeLoadable(SearchUsersUseCase.Params(validationResult))
                .mapResultValue { list -> list.filterNot { it.id == _userLiveData.value?.getOrNull()?.user?.id } }
        )
    }

    fun getUser() {
        _userLiveData.launchLoadData(getStoredUserUseCase.executeLoadable(Unit))
    }

    fun proceedUser(userPreview: UserPreview, removeOnly: Boolean) {
        if (removeOnly) pickedUsers.remove(userPreview) else pickedUsers.addOrRemove(userPreview)
        _addedUsersLiveData.postValue(pickedUsers)
    }

    fun createChannel(memberId: Int) {
        _createChannelLiveEvent.launchLoadData(
            createChannelUseCase.executeLoadable(
                CreateChannelUseCase.Params(
                    name = null,
                    type = ChannelType.PERSONAL,
                    membersIds = listOf(memberId),
                    imagePath = null
                )
            )
        )
    }

    private fun validateSearchQuery(query: String?) = if (!query.isNullOrBlank() && query.length >= MIN_QUERY_LENGTH) {
        query.trim().lowercase()
    } else {
        _searchErrorLiveEvent.postValue(Unit)
        null
    }
}
