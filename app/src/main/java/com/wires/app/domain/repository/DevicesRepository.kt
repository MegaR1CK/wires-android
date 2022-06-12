package com.wires.app.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.wires.app.data.preferences.PreferenceStorage
import com.wires.app.data.remote.WiresApiService
import com.wires.app.data.remote.params.PushTokenRegisterParams
import javax.inject.Inject

class DevicesRepository @Inject constructor(
    private val context: Context,
    private val apiService: WiresApiService,
    private val preferenceStorage: PreferenceStorage
) {
    suspend fun updatePushToken(deviceId: String, name: String, pushToken: String) {
        apiService.registerPushToken(PushTokenRegisterParams(deviceId, name, pushToken))
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).orEmpty()

    fun getPushToken() = preferenceStorage.pushToken

    fun storePushToken(token: String?) {
        preferenceStorage.pushToken = token
    }

    fun getNeedUpdatePushToken() = preferenceStorage.needUpdatePushToken

    fun storeNeedUpdatePushToken(needUpdate: Boolean) {
        preferenceStorage.needUpdatePushToken = needUpdate
    }
}
