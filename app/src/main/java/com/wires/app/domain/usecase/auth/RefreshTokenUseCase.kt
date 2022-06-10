package com.wires.app.domain.usecase.auth

import com.wires.app.data.model.TokenPair
import com.wires.app.domain.repository.TokensRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.domain.usecase.token.StoreTokensUseCase
import javax.inject.Inject

/**
 * Обновление токенов авторизации
 */
class RefreshTokenUseCase @Inject constructor(
    private val tokensRepository: TokensRepository,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val storeTokensUseCase: StoreTokensUseCase
) : UseCaseLoadable<Unit, TokenPair>() {

    override suspend fun execute(params: Unit): TokenPair {
        val tokenPair = tokensRepository.refreshToken(getRefreshTokenUseCase.execute(Unit).orEmpty())
        storeTokensUseCase.execute(StoreTokensUseCase.Params(tokenPair))
        return tokenPair
    }
}
