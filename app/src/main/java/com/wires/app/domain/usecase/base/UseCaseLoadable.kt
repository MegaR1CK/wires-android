package com.wires.app.domain.usecase.base

import com.wires.app.data.LoadableResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class UseCaseLoadable<T, R> {

    abstract suspend fun execute(params: T): R

    fun executeLoadable(params: T): Flow<LoadableResult<R>> = flow {
        try {
            emit(LoadableResult.loading())
            emit(LoadableResult.success(execute(params)))
        } catch (throwable: Throwable) {
            emit(LoadableResult.failure(throwable))
        }
    }
}
