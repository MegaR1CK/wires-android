package com.wires.app.data

import com.wires.app.data.remote.ParsedError
import com.wires.app.extensions.parseError

typealias Loading<T> = LoadableResult.Loading<T>
typealias Success<T> = LoadableResult.Success<T>
typealias Failure<T> = LoadableResult.Failure<T>

/**
 * Usage:
 * when (result) {
 *     is Loading -> { ... }
 *     is Success -> { value ->... }
 *     is Failure -> { throwable ->... }
 * }
 */
sealed class LoadableResult<R> {
    class Loading<R> : LoadableResult<R>()
    class Success<R>(val value: R) : LoadableResult<R>()
    class Failure<R>(val throwable: Throwable, error: ParsedError? = null) : LoadableResult<R>() {
        val error = error ?: throwable.parseError()
    }

    companion object {
        fun <R> loading(): LoadableResult<R> = Loading()
        fun <R> success(value: R): LoadableResult<R> = Success(value)
        fun <R> failure(throwable: Throwable, error: ParsedError? = null): LoadableResult<R> = Failure(throwable, error)
    }

    val isLoading: Boolean get() = this is Loading

    val isSuccess: Boolean get() = this is Success

    val isFailure: Boolean get() = this is Failure

    /**
     * Usage:
     * val product: Product? = result.getOrNull()
     */
    fun getOrNull(): R? = when (this) {
        is Success -> value
        else -> null
    }

    /**
     * The given function is applied if this is a Success.
     * Usage:
     * Success(Product()).map {
     *     Pair(position, it)
     * }
     */
    inline fun <C> map(f: (R) -> C): LoadableResult<C> = when (this) {
        is Loading -> loading()
        is Failure -> failure(throwable)
        is Success -> success(f(value))
    }

    /**
     * Usage:
     * result.doOnComplete {
     *     val value = it.getOrNull()
     *     handleValue(value)
     * }
     */
    inline fun doOnComplete(operation: (LoadableResult<R>) -> Unit) {
        when (this) {
            is Loading -> { /* Skip it */
            }
            is Failure -> operation(failure(throwable))
            is Success -> operation(success(value))
        }
    }

    /**
     * Usage:
     * result.doOnLoading {
     *     showLoading()
     * }
     */
    inline fun doOnLoading(operation: () -> Unit) {
        when (this) {
            is Loading -> operation()
            is Failure -> { /* Skip it */
            }
            is Success -> { /* Skip it */
            }
        }
    }

    /**
     * Usage:
     * result.doOnSuccess {
     *     val value = it.getOrNull()
     *     handleValue(value)
     * }
     */
    inline fun doOnSuccess(operation: (R) -> Unit) {
        when (this) {
            is Loading -> { /* Skip it */
            }
            is Failure -> { /* Skip it */
            }
            is Success -> operation(value)
        }
    }

    /**
     * Usage:
     * result.doOnSuccess {
     *     val value = it.getOrNull()
     *     handleValue(value)
     * }
     */
    inline fun doOnFailure(operation: (ParsedError) -> Unit) {
        when (this) {
            is Loading -> { /* Skip it */
            }
            is Failure -> operation(error)
            is Success -> { /* Skip it */
            }
        }
    }
}
