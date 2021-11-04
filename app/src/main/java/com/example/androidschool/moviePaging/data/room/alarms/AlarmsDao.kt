package com.example.androidschool.moviePaging.data.room.alarms

import androidx.room.*

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    fun getAlarms(): List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE movieId = :movieId")
    suspend fun deleteAlarm(movieId: Int)
}