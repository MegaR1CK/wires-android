package com.wires.app.domain.usecase.devices

import android.os.Build
import com.wires.app.domain.repository.DevicesRepository
import com.wires.app.domain.usecase.base.UseCaseLoadable
import com.wires.app.extensions.parseError
import javax.inject.Inject

/**
 * Обновление пуш-токена на сервере
 */
class UpdatePushTokenUseCase @Inject constructor(
    private val devicesRepository: DevicesRepository
) : UseCaseLoadable<UpdatePushTokenUseCase.Params, Unit>() {

    override suspend fun execute(params: Params): Unit = with(devicesRepository) {
        params.pushToken?.let(::storePushToken)
        val deviceId = getDeviceId()
        val deviceName = Build.MODEL
        try {
            getPushToken()?.let { token ->
                updatePushToken(deviceId, deviceName, token)
            }
            storeNeedUpdatePushToken(false)
        } catch (throwable: Throwable) {
            storeNeedUpdatePushToken(true)
            throwable.parseError()
        }
    }

    data class Params(
        val pushToken: String? = null
    )
}
