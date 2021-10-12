package com.example.androidschool.moviePaging.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmsDatabase: RoomDatabase() {

    abstract fun getAlarmsDao(): AlarmsDao

    companion object {

        @Volatile
        private var database: AlarmsDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AlarmsDatabase? {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    AlarmsDatabase::class.java,
                    "database"
                ).build()
                database as AlarmsDatabase
            } else database as AlarmsDatabase
        }
    }
}