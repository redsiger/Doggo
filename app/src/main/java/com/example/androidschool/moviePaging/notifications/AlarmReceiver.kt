package com.example.androidschool.moviePaging.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.androidschool.moviePaging.data.room.Alarm
import com.example.androidschool.moviePaging.data.room.AlarmsDao
import com.example.androidschool.moviePaging.data.room.AlarmsDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var mAlarmsRoomDao: AlarmsDao

    override fun onReceive(context: Context, intent: Intent) {
        val movieId = intent.getStringExtra("MovieId")
        val movieTitle = intent.getStringExtra("MovieTitle")
        val movieOverview = intent.getStringExtra("MovieOverview")
//        Log.e(intent.action, intent.getStringExtra("MovieId") ?: "problem in id")
//        Log.e(intent.action, intent.getStringExtra("MovieTitle") ?: "problem in title")
//        Log.e(intent.action, intent.getStringExtra("MovieOverview") ?: "problem in overview")

        val bundle = Bundle()
        var movieIdInt: Int = 500
        var movieTitleString: String = "550"
        var movieOverviewString: String = "555"
        if (!movieId.isNullOrBlank()) {
            movieIdInt = movieId.toInt()
        }
        if (!movieTitle.isNullOrBlank()) {
            movieTitleString = movieTitle
        }
        if (!movieOverview.isNullOrBlank()) {
            movieOverviewString = movieOverview
        }
        bundle.putInt("MovieId", movieIdInt)
        AppNotification(context).pushNotification(
            context = context,
            channel = CHANNEL_1_ID,
            title = movieTitleString,
            text = movieOverviewString,
            bundle = bundle,
            movieIdInt
        )

//        val mAlarmsRoomDao = AlarmsDatabase.getInstance(context)!!.getAlarmsDao()
        GlobalScope.launch(Dispatchers.IO) {
            mAlarmsRoomDao.deleteAlarm(movieIdInt)
        }

        var msg: String = intent.action ?: "action is null"
        msg += " "
        msg += movieId
        Log.e("AlarmReceiver", msg)
    }
}