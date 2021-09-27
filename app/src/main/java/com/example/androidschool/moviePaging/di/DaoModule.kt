package com.example.androidschool.moviePaging.di

import android.app.Application
import android.content.Context
import com.example.androidschool.moviePaging.data.dao.database.MovieSearchResponseDao
import com.example.androidschool.moviePaging.data.dao.database.MovieSearchResponseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Singleton
    @Provides
    fun provideMovieDao(context: Context): MovieSearchResponseDao {
        return MovieSearchResponseDatabase.getInstance(context).movieDao
    }
}