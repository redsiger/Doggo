package com.example.androidschool.moviePaging.data.dao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidschool.moviePaging.data.dao.DaoMovieSearchResponseWithMovies

@Database(entities = [DaoMovieSearchResponseWithMovies::class], version = 1)
abstract class MovieSearchResponseDatabase: RoomDatabase() {

    companion object {
        fun getInstance(context: Context): MovieSearchResponseDatabase {
            return Room.databaseBuilder(context, MovieSearchResponseDatabase::class.java, "name").build()
        }
    }
}