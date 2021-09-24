package com.example.androidschool.moviePaging.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GenreIds")
data class DaoMovieGenreId(
    @PrimaryKey
    val genreId: Int
)