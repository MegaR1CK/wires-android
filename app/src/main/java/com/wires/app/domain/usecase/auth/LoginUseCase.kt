package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.domain.usecase.token.StoreTokenUseCase
import com.wires.app.domain.usecase.user.GetCurrentUserUseCase
import com.wires.app.domain.usecase.user.StoreUserUseCase
import javax.inject.Inject

/**
 * Вход в аккаунт пользователя с сохранением токена и пользователя
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val storeUserUseCase: StoreUserUseCase,
    private val storeTokenUseCase: StoreTokenUseCase,
    private val getPasswordHashUseCase: GetPasswordHashUseCase
) : UseCaseLoadable<LoginUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val passwordHash = getPasswordHashUseCase.execute(GetPasswordHashUseCase.Params(params.email, params.password))
        val token = authRepository.loginUser(params.email, passwordHash)
        storeTokenUseCase.execute(StoreTokenUseCase.Params(token))
        storeUserUseCase.execute(StoreUserUseCase.Params(getCurrentUserUseCase.execute(Unit)))
    }

    data class Params(
        val email: String,
        val password: String
    )
}
