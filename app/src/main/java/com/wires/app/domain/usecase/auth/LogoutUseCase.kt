package com.wires.app.domain.usecase.auth

import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.domain.repository.AuthRepository
import com.wires.app.domain.repository.DevicesRepository
import com.wires.app.domain.repository.UserRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Выход из аккаунта пользователя, удаление всех локальных данных
 */
class LogoutUseCase @Inject constructor(
    private val devicesRepository: DevicesRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val preferenceStorage: PreferenceStorage
) : UseCaseLoadable<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        authRepository.logoutUser(devicesRepository.getDeviceId())
        preferenceStorage.clearStorage()
        userRepository.clearUser()
    }
}
