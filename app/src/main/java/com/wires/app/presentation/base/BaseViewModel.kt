package com.wires.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wires.app.data.LoadableResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun <T> loadData(
        block: suspend () -> T,
    ): Flow<LoadableResult<T>> = flow {
        try {
            emit(LoadableResult.loading())
            emit(LoadableResult.success(block()))
        } catch (error: Throwable) {
            if (error !is CancellationException) {
                emit(LoadableResult.failure(error))
            }
        }
    }

    protected fun <T> loadData(
        block: Flow<T>,
    ): Flow<LoadableResult<T>> = flow {
        try {
            emit(LoadableResult.loading())
            block.collect {
                emit(LoadableResult.success(it))
            }
        } catch (error: Throwable) {
            emit(LoadableResult.failure(error))
        }
    }

    protected fun <T> MutableLiveData<LoadableResult<T>>.launchLoadData(
        block: Flow<LoadableResult<T>>,
    ): Job = viewModelScope.launch {
        block.collect { result ->
            this@launchLoadData.postValue(result)
        }
    }

    protected fun <T> MutableLiveData<LoadableResult<T>>.launchLoadData(
        block: suspend () -> T,
    ): Job = viewModelScope.launch {
        loadData(block).collect { result -> this@launchLoadData.postValue(result) }
    }

    protected fun <T : Any> MutableLiveData<PagingData<T>>.launchPagingData(
        block: Flow<PagingData<T>>
    ): Job = viewModelScope.launch {
        block
            .cachedIn(viewModelScope)
            .collectLatest { this@launchPagingData.postValue(it) }
    }

    protected fun MutableLiveData<LoadableResult<Unit>>.bindPagingState(loadState: CombinedLoadStates) {
        when (loadState.source.refresh) {
            // Only show the list if refresh succeeds.
            is LoadState.NotLoading -> this.postValue(LoadableResult.success(Unit))
            // Show loading spinner during initial load or refresh.
            is LoadState.Loading -> this.postValue(LoadableResult.loading())
            // Show the retry state if initial load or refresh fails.
            is LoadState.Error -> this.postValue(LoadableResult.failure((loadState.source.refresh as LoadState.Error).error))
        }
    }
}
