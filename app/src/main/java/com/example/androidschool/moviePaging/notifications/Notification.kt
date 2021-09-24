package com.example.androidschool.moviePaging.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androidschool.moviePaging.R

const val CHANNEL_1_ID: String = "1"

class AppNotification: Application() {


    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_1_ID
            val channelName = "channel 1"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channel1 = NotificationChannel(channelId, channelName, channelImportance)
            channel1.description = "This is a Channel 1"

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)
        }
    }

    fun createNotification(
        context: Context,
        channel: String,
        title: String,
        text: String,
        notificationId: Int
    ) {
        val builder = NotificationCompat.Builder(
            context,
            channel
        )
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}