package com.example.androidschool.moviePaging.di

import android.content.Context
import com.example.androidschool.moviePaging.data.room.AppDatabase
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import com.example.androidschool.moviePaging.data.room.cache.MovieSearchResponseDao
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
    fun provideAlarmDao(@ApplicationContext context: Context): AlarmsDao {
        return AppDatabase.getInstance(context)!!.getAlarmsDao()
    }

    @Singleton
    @Provides
    fun provideMoviesDao(@ApplicationContext context: Context): MovieSearchResponseDao {
        return AppDatabase.getInstance(context)!!.getMoviesDao()
    }
}