package com.wires.app.domain.usecase.user

import com.wires.app.data.model.User
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Сохранение пользователя в бд
 */
class StoreUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<StoreUserUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        userRepository.storeUser(params.user)
    }

    data class Params(
        val user: User
    )
}
