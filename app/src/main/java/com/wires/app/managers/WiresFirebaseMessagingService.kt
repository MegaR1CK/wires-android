package com.wires.app.managers

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wires.app.domain.usecase.devices.UpdatePushTokenUseCase
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WiresFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var updatePushTokenUseCase: UpdatePushTokenUseCase

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            updatePushTokenUseCase.executeLoadable(UpdatePushTokenUseCase.Params(token)).collect { result ->
                result.doOnFailure { Timber.e(it.message) }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}
