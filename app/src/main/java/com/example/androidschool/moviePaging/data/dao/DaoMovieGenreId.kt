package com.example.androidschool.moviePaging.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GenreIds")
data class DaoMovieGenreId(
    @PrimaryKey(autoGenerate = false)
    val genreId: Int
)