package com.wires.app.domain.usecase.base

interface UseCaseAsync<T, R> {
    suspend fun execute(params: T): R
}
