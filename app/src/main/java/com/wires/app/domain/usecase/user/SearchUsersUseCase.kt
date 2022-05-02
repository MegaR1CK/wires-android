package com.wires.app.domain.usecase.user

import com.wires.app.data.model.UserPreview
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Поиск пользователей по имени пользователя
 */
class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCaseLoadable<SearchUsersUseCase.Params, List<UserPreview>>() {

    override suspend fun execute(params: Params): List<UserPreview> {
        return userRepository.searchUsers(params.query)
    }

    data class Params(
        val query: String
    )
}
