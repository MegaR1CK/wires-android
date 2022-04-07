package com.wires.app.domain.usecase.token

import com.wires.app.data.model.Token
import com.wires.app.domain.repository.TokenRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Сохранение токена авторизации
 */
class StoreTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) : UseCaseLoadable<StoreTokenUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        tokenRepository.setAccessToken(params.token.token)
    }

    data class Params(
        val token: Token
    )
}
