package com.example.androidschool.moviePaging.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.androidschool.moviePaging.MainActivity
import com.example.androidschool.moviePaging.R

const val CHANNEL_1_ID: String = "1"

class AppNotification(private val mContext: Context) {

    private var mNotificationManager: NotificationManagerCompat = NotificationManagerCompat.from(mContext)
    private var mAlarmManager: AlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        Log.e("AppNotificationContext", mContext.toString())
    }

    fun pushNotification(
        context: Context,
        channel: String,
        title: String,
        text: String,
        bundle: Bundle,
        notificationId: Int
    ) {
        val activityIntent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0 )
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .addDestination(R.id.movieDetailsFragment, bundle)
            .createPendingIntent()

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
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        mNotificationManager.notify(notificationId, builder.build())
        Log.e("CreateNotification", context.toString())
    }

    fun deleteNotification(context: Context, notificationId: Int) {
        mNotificationManager.cancel(notificationId)
        Log.e("DeleteNotification", context.toString())
    }
}