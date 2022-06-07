package com.wires.app.domain.usecase.user

import com.wires.app.data.model.UserInterest
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Обновление пользователя
 */
class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<UpdateUserUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        userRepository.updateUser(
            params.firstName,
            params.lastName,
            params.email,
            params.username,
            params.interests,
            params.avatarPath
        )
        userRepository.storeUser(userRepository.getCurrentUser())
    }

    data class Params(
        val username: String? = null,
        val email: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val interests: List<UserInterest> = emptyList(),
        val avatarPath: String? = null,
    )
}
