package com.wires.app.domain.usecase

import com.wires.app.domain.repository.DevicesRepository
import com.wires.app.domain.usecase.auth.IsSignedInUseCase
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.domain.usecase.devices.UpdatePushTokenUseCase
import com.wires.app.presentation.splash.SplashResult
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Юзкейс входного потока с возможным обновлением пуш-токена
 */
class SplashUseCase @Inject constructor(
    private val isSignedInUseCase: IsSignedInUseCase,
    private val devicesRepository: DevicesRepository,
    private val updatePushTokenUseCase: UpdatePushTokenUseCase
) : UseCaseLoadable<Unit, SplashResult>() {

    companion object {
        private const val SPLASH_MIN_DELAY = 2500L
    }

    override suspend fun execute(params: Unit): SplashResult {
        delay(SPLASH_MIN_DELAY)
        if (devicesRepository.getNeedUpdatePushToken()) {
            updatePushTokenUseCase.execute(UpdatePushTokenUseCase.Params())
        }
        return if (isSignedInUseCase.execute(Unit)) {
            SplashResult.MAIN_SCREEN
        } else {
            SplashResult.AUTH
        }
    }
}
