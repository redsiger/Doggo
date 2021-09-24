package com.example.androidschool.moviePaging.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AppNotification().createNotification(
            context = context,
            channel = CHANNEL_1_ID,
            title = "Title",
            text = "Text",
            notificationId = 1
        )
    }
}