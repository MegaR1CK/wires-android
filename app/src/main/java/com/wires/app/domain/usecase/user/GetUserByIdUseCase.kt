package com.wires.app.domain.usecase.user

import com.wires.app.data.model.UserWrapper
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Получение пользователя с сервера по id
 */
class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<GetUserByIdUseCase.Params, UserWrapper>() {

    override suspend fun execute(params: Params): UserWrapper {
        return UserWrapper(userRepository.getUser(params.userId))
    }

    data class Params(
        val userId: Int
    )
}
