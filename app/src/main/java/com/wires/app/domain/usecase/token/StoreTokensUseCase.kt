package com.wires.app.domain.usecase.token

import com.wires.app.data.model.TokenPair
import com.wires.app.domain.repository.TokensRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Сохранение токенов авторизации
 */
class StoreTokensUseCase @Inject constructor(
    private val tokensRepository: TokensRepository
) : UseCaseLoadable<StoreTokensUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) = with(tokensRepository) {
        setAccessToken(params.tokenPair.accessToken)
        setRefreshToken(params.tokenPair.refreshToken)
    }

    data class Params(
        val tokenPair: TokenPair
    )
}
