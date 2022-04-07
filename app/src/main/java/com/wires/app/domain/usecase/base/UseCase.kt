package com.wires.app.domain.usecase.base

interface UseCase<T, R> {
    fun execute(params: T): R
}
