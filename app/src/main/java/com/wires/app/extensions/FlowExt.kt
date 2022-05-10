package com.wires.app.extensions

import androidx.paging.PagingData
import androidx.paging.map
import com.wires.app.data.LoadableResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<LoadableResult<T>>.mapResultValue(action: (T) -> R) =
    map { result -> result.map { value -> action(value) } }

fun <T : Any, R : Any> Flow<PagingData<T>>.mapPagingValue(action: (T) -> R) =
    map { result -> result.map { value -> action(value) } }
