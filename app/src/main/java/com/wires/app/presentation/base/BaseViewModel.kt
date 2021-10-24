package com.wires.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wires.app.data.LoadableResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

abstract class BaseViewModel : ViewModel() {

    protected fun <T> MutableLiveData<LoadableResult<T>>.launchLoadData(
        block: Flow<LoadableResult<T>>,
    ): Job = viewModelScope.launch {
        block.collect { result ->
            this@launchLoadData.postValue(result)
        }
    }

    protected fun <T : Any> MutableLiveData<PagingData<T>>.launchPagingData(
        block: () -> Flow<PagingData<T>>
    ): Job = viewModelScope.launch {
        block()
            .cachedIn(viewModelScope)
            .collectLatest { this@launchPagingData.postValue(it) }
    }
}