package com.wires.app.data.model

import com.wires.app.data.remote.ParsedError

/**
 * Результат установки лайка, который даже при ошибке содержит postId для обновления
 */
data class SetLikeResult(
    val postId: Int,
    val error: ParsedError?
)
