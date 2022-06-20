package com.wires.app.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wires.app.R
import com.wires.app.domain.usecase.devices.UpdatePushTokenUseCase
import com.wires.app.presentation.main.MainActivity
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
        private const val NOTIFICATION_IMAGE_KEY = "image"
        private const val NOTIFICATION_INTENT_REQUEST_CODE = 1
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
        val title = messageData[NOTIFICATION_TITLE_KEY].orEmpty()
        val body = messageData[NOTIFICATION_BODY_KEY].orEmpty()
        val imageUrl = messageData[NOTIFICATION_IMAGE_KEY]
        if (imageUrl.isNullOrEmpty()) {
            showNotification(title, body)
        } else {
            getBitmapFromUrl(imageUrl) { bitmap -> showNotification(title, body, bitmap) }
        }
    }

    private fun showNotification(title: String, body: String, image: Bitmap? = null) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_messages)
            .setLargeIcon(image)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(createNotificationIntent())
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(Math.random().toInt(), notification)
    }

    private fun getBitmapFromUrl(url: String, action: (Bitmap) -> Unit) =
        Glide.with(context).asBitmap().load(url).into(
            object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) = Unit
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) = action(resource)
            }
        )

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

    private fun createNotificationIntent() = PendingIntent.getActivity(
        context,
        NOTIFICATION_INTENT_REQUEST_CODE,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}
