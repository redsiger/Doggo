package com.example.androidschool.moviePaging.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.androidschool.moviePaging.MainActivity
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import kotlinx.coroutines.*

const val CHANNEL_1_ID: String = "1"

class AppNotification(
    val mContext: Context,
    val mAlarmManager: AlarmManager,
    val mNotificationManager: NotificationManagerCompat,
    val mAlarmsDao: AlarmsDao,
    val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun createNotificationAndAlarm(movieId: Int, movieTitle: String, time: Long) {
        createNotification(movieId, movieTitle, time)
        createAlarm(movieId, movieTitle, time)
    }

    fun createNotification(movieId: Int, movieTitle: String, time: Long) {
        val pendingIntent = createPendingIntent(movieId, movieTitle)

        val sdk = Build.VERSION.SDK_INT
        when {
            sdk >= Build.VERSION_CODES.M -> {
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
            sdk >= Build.VERSION_CODES.LOLLIPOP -> {
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
            else -> {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            }
        }
    }

    suspend fun createAlarm(movieId: Int, movieTitle: String, time: Long) {
//        CoroutineScope(defaultDispatcher).launch {
            val alarm = Alarm(
                movieId,
                movieTitle,
                time
            )
            mAlarmsDao.addAlarm(alarm)
//        }
    }

    suspend fun deleteNotificationAndAlarm(movieId: Int, movieTitle: String) {
        deleteNotification(movieId, movieTitle)
        deleteAlarm(movieId)
    }

    fun deleteNotification(movieId: Int, movieTitle: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.cancel(createPendingIntent(movieId, movieTitle))
        }
    }

    suspend fun deleteAlarm(movieId: Int) {
//        CoroutineScope(defaultDispatcher).launch {
            mAlarmsDao.deleteAlarm(movieId)
//        }
    }

    private fun createPendingIntent(movieId: Int, movieTitle: String): PendingIntent {
        val intent = Intent(mContext, AlarmReceiver::class.java)
        intent.action = "PUSH_NOTIFICATION $movieTitle"
        intent.putExtra("MovieId", movieId.toString())
        intent.putExtra("MovieTitle", movieTitle)

        return PendingIntent.getBroadcast(mContext, 0, intent, 0)
    }


    fun pushNotification(
        channel: String,
        title: String,
        bundle: Bundle,
        notificationId: Int
    ) {
        val pendingIntent = NavDeepLinkBuilder(mContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .addDestination(R.id.movieDetailsFragment, bundle)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(
            mContext,
            channel
        )
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(mContext.getString(R.string.notification_text_remind))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(mContext.getString(R.string.notification_text_remind))
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notification = builder.build()

        mNotificationManager.notify(notificationId, notification)
    }

    fun deleteNotification(notificationId: Int) {
        mNotificationManager.cancel(notificationId)
    }
}