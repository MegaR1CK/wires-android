package com.wires.app.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wires.app.R
import com.wires.app.domain.usecase.devices.UpdatePushTokenUseCase
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WiresFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "channel_messages"
        private const val NOTIFICATION_TITLE_KEY = "title"
        private const val NOTIFICATION_BODY_KEY = "body"
    }

    @Inject lateinit var updatePushTokenUseCase: UpdatePushTokenUseCase
    @Inject lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()
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
        val messageData = message.data.toMap()
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_messages)
            .setContentTitle(messageData[NOTIFICATION_TITLE_KEY])
            .setContentText(messageData[NOTIFICATION_BODY_KEY])
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(Math.random().toInt(), notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notifications_messages_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
}
