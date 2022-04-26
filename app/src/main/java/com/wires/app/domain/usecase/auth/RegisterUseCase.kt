package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Регистрация аккаунта пользователя с последующим входом
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase,
    private val getPasswordHashUseCase: GetPasswordHashUseCase
) : UseCaseLoadable<RegisterUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val passwordHash = getPasswordHashUseCase.execute(GetPasswordHashUseCase.Params(params.password))
        authRepository.registerUser(params.username, params.email, passwordHash)
        loginUseCase.execute(LoginUseCase.Params(params.email, params.password))
    }

    data class Params(
        val username: String,
        val email: String,
        val password: String
    )
}
