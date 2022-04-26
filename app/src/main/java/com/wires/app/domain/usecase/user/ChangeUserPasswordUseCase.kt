package com.wires.app.domain.usecase.user

import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.auth.GetPasswordHashUseCase
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Изменение пароля пользователя
 */
class ChangeUserPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getPasswordHashUseCase: GetPasswordHashUseCase
) : UseCaseLoadable<ChangeUserPasswordUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val oldPasswordHash = getPasswordHashUseCase.execute(GetPasswordHashUseCase.Params(params.email, params.oldPassword))
        val newPasswordHash = getPasswordHashUseCase.execute(GetPasswordHashUseCase.Params(params.email, params.newPassword))
        userRepository.changeUserPassword(oldPasswordHash, newPasswordHash)
    }

    data class Params(
        val email: String,
        val oldPassword: String,
        val newPassword: String
    )
}
