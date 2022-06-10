package com.wires.app.domain.usecase.auth

import com.wires.app.domain.repository.TokensRepository
import com.wires.app.domain.usecase.base.UseCase
import javax.inject.Inject

/**
 * Получение токена обновления
 */
class GetRefreshTokenUseCase @Inject constructor(
    private val tokensRepository: TokensRepository
) : UseCase<Unit, String?> {

    override fun execute(params: Unit): String? {
        return tokensRepository.getRefreshToken()
    }
}
