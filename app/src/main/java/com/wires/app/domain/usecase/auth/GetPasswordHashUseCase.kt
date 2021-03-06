package com.wires.app.domain.usecase.auth

import com.wires.app.domain.usecase.base.UseCase
import com.wires.app.managers.Cryptor
import javax.inject.Inject

/**
 * Получение хэша пароля
 */
class GetPasswordHashUseCase @Inject constructor(
    private val cryptor: Cryptor
) : UseCase<GetPasswordHashUseCase.Params, String> {

    override fun execute(params: Params): String {
        return cryptor.getSha256Hash(params.password)
    }

    data class Params(
        val password: String
    )
}
