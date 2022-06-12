package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.repository.DevicesRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.domain.usecase.token.StoreTokensUseCase
import com.wires.app.domain.usecase.user.GetCurrentUserUseCase
import com.wires.app.domain.usecase.user.StoreUserUseCase
import javax.inject.Inject

/**
 * Вход в аккаунт пользователя с сохранением токена и пользователя
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val devicesRepository: DevicesRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val storeUserUseCase: StoreUserUseCase,
    private val storeTokensUseCase: StoreTokensUseCase,
    private val getPasswordHashUseCase: GetPasswordHashUseCase,
) : UseCaseLoadable<LoginUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val passwordHash = getPasswordHashUseCase.execute(GetPasswordHashUseCase.Params(params.password))
        val tokenPair = authRepository.loginUser(params.email, passwordHash, devicesRepository.getDeviceId())
        storeTokensUseCase.execute(StoreTokensUseCase.Params(tokenPair))
        storeUserUseCase.execute(StoreUserUseCase.Params(getCurrentUserUseCase.execute(Unit)))
    }

    data class Params(
        val email: String,
        val password: String
    )
}
