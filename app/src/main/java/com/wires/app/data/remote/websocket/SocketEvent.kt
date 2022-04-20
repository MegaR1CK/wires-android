package com.wires.app.data.remote.websocket

sealed class SocketEvent<T> {
    class OpenEvent<T> : SocketEvent<T>()
    data class CloseEvent<T>(val code: Int, val reason: String) : SocketEvent<T>()
    data class Error<T>(val error: Throwable) : SocketEvent<T>()
    data class Message<T>(val content: T) : SocketEvent<T>()

    inline fun doOnMessage(block: (T) -> Unit) {
        if (this is Message) block(content)
    }

    inline fun doOnOpen(block: () -> Unit) {
        if (this is OpenEvent) block()
    }

    inline fun doOnError(block: (Throwable) -> Unit) {
        if (this is Error) block(error)
    }

    fun <R> transform(operation: (T) -> R) = when (this) {
        is OpenEvent -> OpenEvent()
        is CloseEvent -> CloseEvent(code, reason)
        is Error -> Error(error)
        is Message -> Message(operation(content))
    }
}
