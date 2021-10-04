package com.example.androidschool.moviePaging.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = Bundle()
        bundle.putString("MovieId", "550")
        bundle.putString("MovieTitle", "movieTitle")
        AppNotification(context).pushNotification(
            context = context,
            channel = CHANNEL_1_ID,
            title = "Title",
            text = "Text",
            bundle = bundle,
            notificationId = 1
        )
    }
}