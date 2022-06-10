package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.TokensRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCase
import javax.inject.Inject

/**
 * Выход из аккаунта пользователя, удаление всех локальных данных
 */
class LogoutUseCase @Inject constructor(
    private val tokensRepository: TokensRepository,
    private val userRepository: UserRepository
) : UseCase<Unit, Unit> {
    override fun execute(params: Unit) {
        tokensRepository.setAccessToken(null)
        userRepository.clearUser()
    }
}
