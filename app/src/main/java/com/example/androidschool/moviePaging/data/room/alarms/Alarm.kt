package com.example.androidschool.moviePaging.data.room.alarms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val movieTitle: String,
    val time: Long
)