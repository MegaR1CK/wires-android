package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCase
import javax.inject.Inject

/**
 * Проверка на вход в аккаунт
 */
class IsSignedInUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<Unit, Boolean> {
    override fun execute(params: Unit): Boolean {
        return userRepository.isSignedIn
    }
}
