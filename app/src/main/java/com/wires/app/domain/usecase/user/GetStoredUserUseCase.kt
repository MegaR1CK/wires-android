package com.wires.app.domain.usecase.user

import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение пользователя из бд
 */
class GetStoredUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<Unit, UserWrapper>() {

    override suspend fun execute(params: Unit): UserWrapper {
        return UserWrapper(userRepository.getStoredUser())
    }
}
