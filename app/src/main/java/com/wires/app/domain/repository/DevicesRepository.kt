package com.wires.app.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.DeviceRegisterParams
import javax.inject.Inject

class DevicesRepository @Inject constructor(
    private val context: Context,
    private val apiService: WiresApiService
) {
    suspend fun registerDevice(deviceId: String, name: String, pushToken: String) {
        apiService.registerDevice(DeviceRegisterParams(deviceId, name, pushToken))
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).orEmpty()
}
