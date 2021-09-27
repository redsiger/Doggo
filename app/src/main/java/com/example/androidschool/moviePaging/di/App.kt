package com.example.androidschool.moviePaging.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.androidschool.moviePaging.data.dao.database.MovieSearchResponseDatabase
import com.example.androidschool.moviePaging.notifications.CHANNEL_1_ID

//const val CHANNEL_1_ID: String = "1"
lateinit var APPLICATION_CONTEXT: Application

class App: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        initRoom()
        createNotificationChannels()
    }

    private fun initRoom() {
        APPLICATION_CONTEXT = this
//        val roomDao = MovieSearchResponseDatabase.getInstance(this).movieDao
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

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .build()
    }


}