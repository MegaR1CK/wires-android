package com.wires.app.domain.usecase.devices

import android.os.Build
import com.wires.app.domain.repository.DevicesRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import javax.inject.Inject

/**
 * Регистрация девайса для получения уведомления
 */
class RegisterDeviceUseCase @Inject constructor(
    private val devicesRepository: DevicesRepository
) : UseCaseLoadable<RegisterDeviceUseCase.Params, Unit>() {

    override suspend fun execute(params: Params) {
        val deviceId = devicesRepository.getDeviceId()
        val deviceName = Build.MODEL
        devicesRepository.registerDevice(deviceId, deviceName, params.pushToken)
    }

    data class Params(
        val pushToken: String
    )
}
