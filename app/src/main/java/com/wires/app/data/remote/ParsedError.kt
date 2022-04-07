package com.wires.app.data.remote

import com.google.gson.annotations.SerializedName
import java.net.HttpURLConnection

sealed class ParsedError(val code: String, val title: String, val message: String)

class NetworkError(code: String, title: String, message: String) : ParsedError(code, title, message)
class GeneralError(code: String, title: String, message: String) : ParsedError(code, title, message)
class UnauthorizedError(code: String, title: String, message: String) : ParsedError(code, title, message)

data class ApiErrorResponse(
    @SerializedName("error") val error: ApiError?
) {
    fun toParsedError(code: Int): ParsedError {
        return when (code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> UnauthorizedError(
                error?.code ?: DEFAULT_ERROR_CODE,
                error?.title ?: DEFAULT_ERROR_TITLE,
                error?.message ?: DEFAULT_ERROR_MESSAGE
            )
            else -> GeneralError(
                error?.code ?: DEFAULT_ERROR_CODE,
                error?.title ?: DEFAULT_ERROR_TITLE,
                error?.message ?: DEFAULT_ERROR_MESSAGE
            )
        }
    }
}

data class ApiError(
    @SerializedName("code") val code: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("message") val message: String?
)

const val DEFAULT_ERROR_CODE = "TEXT_ERROR"
lateinit var DEFAULT_ERROR_MESSAGE: String
lateinit var DEFAULT_ERROR_TITLE: String
