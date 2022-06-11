package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.domain.usecase.token.StoreTokensUseCase
import javax.inject.Inject

/**
 * Выход из аккаунта пользователя, удаление всех локальных данных
 */
class LogoutUseCase @Inject constructor(
    private val storeTokensUseCase: StoreTokensUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : UseCaseLoadable<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        authRepository.logoutUser(getRefreshTokenUseCase.execute(Unit).orEmpty())
        storeTokensUseCase.execute(StoreTokensUseCase.Params(null))
        userRepository.clearUser()
    }
}
