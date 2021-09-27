package com.example.androidschool.moviePaging.data.dao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidschool.moviePaging.data.dao.*

@Database(entities = [
    DaoMovieSearchResponse::class,
    DaoMovie::class,
    DaoMovieGenreId::class,
    DaoMovieGenreIdCrossRef::class],
    version = 1)
abstract class MovieSearchResponseDatabase: RoomDatabase() {

    abstract val movieDao: MovieSearchResponseDao

    companion object {
        @Volatile
        private var INSTANCE: MovieSearchResponseDatabase? = null

        fun getInstance(context: Context): MovieSearchResponseDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    MovieSearchResponseDatabase::class.java,
                    "name"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}