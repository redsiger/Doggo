package com.example.androidschool.moviePaging.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import com.example.androidschool.moviePaging.data.room.cache.MovieSearchResponseDao
import com.example.androidschool.moviePaging.data.room.cache.MovieSearchResponseWithoutMovies
import com.example.androidschool.moviePaging.model.Movie

@Database(entities = [
    Alarm::class,
    MovieSearchResponseWithoutMovies::class,
    Movie::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAlarmsDao(): AlarmsDao
    abstract fun getMoviesDao(): MovieSearchResponseDao

    companion object {

        @Volatile
        private var database: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppDatabase? {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "database"
                ).build()
                database as AppDatabase
            } else database as AppDatabase
        }
    }
}