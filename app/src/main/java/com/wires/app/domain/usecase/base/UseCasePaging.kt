package com.wires.app.domain.usecase.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

abstract class UseCasePaging<T, R : Any> {
    abstract fun execute(params: T): Flow<PagingData<R>>
}
