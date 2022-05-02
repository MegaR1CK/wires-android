package com.wires.app.presentation.pickusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.UserPreview
import com.wires.app.domain.usecase.user.SearchUsersUseCase
import com.wires.app.presentation.base.BaseViewModel
import com.wires.app.presentation.base.SingleLiveEvent
import javax.inject.Inject

class PickUsersViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : BaseViewModel() {

    companion object {
        private const val MIN_QUERY_LENGTH = 3
    }

    private val _searchResultLiveData = MutableLiveData<LoadableResult<List<UserPreview>>>()
    val searchResultLiveData: LiveData<LoadableResult<List<UserPreview>>> = _searchResultLiveData

    private val _searchErrorLiveEvent = SingleLiveEvent<Unit>()
    val searchErrorLiveEvent: LiveData<Unit> = _searchErrorLiveEvent

    val pickedUsersIds = mutableListOf<Int>()

    var lastSearchQuery: String? = null

    fun search(query: String? = lastSearchQuery) = validateSearchQuery(query)?.let { validationResult ->
        lastSearchQuery = validationResult
        _searchResultLiveData.launchLoadData(searchUsersUseCase.executeLoadable(SearchUsersUseCase.Params(validationResult)))
    }

    private fun validateSearchQuery(query: String?) = if (!query.isNullOrBlank() && query.length >= MIN_QUERY_LENGTH) {
        query.trim()
    } else {
        _searchErrorLiveEvent.postValue(Unit)
        null
    }
}
