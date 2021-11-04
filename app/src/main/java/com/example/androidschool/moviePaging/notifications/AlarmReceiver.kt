package com.example.androidschool.moviePaging.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver(): BroadcastReceiver() {

    @Inject
    lateinit var mAlarmsRoomDao: AlarmsDao
    @Inject
    lateinit var mAppNotification: AppNotification

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("receiver", context.toString())
        val movieId = intent.getStringExtra("MovieId")
        val movieTitle = intent.getStringExtra("MovieTitle")

        val bundle = Bundle()
        var movieIdInt: Int = 500
        var movieTitleString: String = "550"
        if (!movieId.isNullOrBlank()) {
            movieIdInt = movieId.toInt()
        }
        if (!movieTitle.isNullOrBlank()) {
            movieTitleString = movieTitle
        }
        bundle.putInt("MovieId", movieIdInt)
        mAppNotification.pushNotification(
            channel = CHANNEL_1_ID,
            title = movieTitleString,
            bundle = bundle,
            movieIdInt
        )
        CoroutineScope(Dispatchers.Default).launch {
            mAppNotification.deleteAlarm(movieIdInt)
        }

        var msg: String = intent.action ?: "action is null"
        msg += " "
        msg += movieId
        Log.e("AlarmReceiver", msg)
    }
}