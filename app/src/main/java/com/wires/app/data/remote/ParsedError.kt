package com.wires.app.data.remote

import com.google.gson.annotations.SerializedName

sealed class ParsedError(val code: String, val message: String)

class NetworkError(code: String, message: String) : ParsedError(code, message)
class GeneralError(code: String, message: String) : ParsedError(code, message)
class UnauthorizedError(code: String, message: String) : ParsedError(code, message)

data class ApiErrorResponse(
    @SerializedName("error") val error: ApiError?
) {
    fun toParsedError(code: Int): ParsedError {
        return GeneralError(
            error?.code ?: DEFAULT_ERROR_CODE,
            error?.message ?: DEFAULT_ERROR_MESSAGE
        )
    }
}

data class ApiError(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?
)

const val DEFAULT_ERROR_CODE = "TEXT_ERROR"
const val DEFAULT_ERROR_MESSAGE = "ERROR"
