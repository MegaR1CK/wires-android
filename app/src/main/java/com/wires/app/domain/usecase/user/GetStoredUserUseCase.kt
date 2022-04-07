package com.wires.app.domain.usecase.user

import com.wires.app.data.model.User
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение пользователя из бд
 */
class GetStoredUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<Unit, User?>() {

    override suspend fun execute(params: Unit): User? {
        return userRepository.getStoredUser()
    }
}
