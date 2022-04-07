package com.wires.app.extensions

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.wires.app.data.remote.ApiErrorResponse
import com.wires.app.data.remote.DEFAULT_ERROR_CODE
import com.wires.app.data.remote.DEFAULT_ERROR_MESSAGE
import com.wires.app.data.remote.DEFAULT_ERROR_TITLE
import com.wires.app.data.remote.GeneralError
import com.wires.app.data.remote.NetworkError
import com.wires.app.data.remote.ParsedError
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.parseError(): ParsedError {
    Timber.e(this)

    val code = DEFAULT_ERROR_CODE
    val title = DEFAULT_ERROR_TITLE
    val message = DEFAULT_ERROR_MESSAGE

    return when {
        isNetworkError -> NetworkError(code, title, message)
        /** General Http error */
        this is HttpException -> {
            val body = response()?.errorBody()
            val gson = GsonBuilder().create()
            var error: ParsedError? = null
            try {
                val apiError = gson.fromJson(body?.string(), ApiErrorResponse::class.java)
                response()?.code()?.let { error = apiError?.toParsedError(it) }
            } catch (ioEx: IOException) {
                Timber.e(ioEx)
            } catch (isEx: IllegalStateException) {
                Timber.e(isEx)
            } catch (isEx: JsonSyntaxException) {
                Timber.e(isEx)
            }
            error ?: GeneralError(code, title, message)
        }
        else -> {
            GeneralError(code, title, message)
        }
    }
}

val Throwable.isNetworkError: Boolean
    get() = when (this) {
        is ConnectException,
        is UnknownHostException,
        is SocketTimeoutException -> true
        else -> false
    }
