package com.example.androidschool.moviePaging.di

import android.content.Context
import com.example.androidschool.moviePaging.data.room.AlarmsDao
import com.example.androidschool.moviePaging.data.room.AlarmsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Room {

    @Singleton
    @Provides
    fun provideRoomDao(@ApplicationContext context: Context): AlarmsDao {
        return AlarmsDatabase.getInstance(context)!!.getAlarmsDao()
    }
}