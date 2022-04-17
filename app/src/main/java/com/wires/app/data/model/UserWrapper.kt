package com.wires.app.data.model

/**
 * Обертка, чтобы возвращать nullable пользователя из usecase
 */
data class UserWrapper(
    val user: User?
)
