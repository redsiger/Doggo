package com.example.androidschool.moviePaging.data.dao

import androidx.room.Entity

@Entity(primaryKeys = ["genreId", "movieId"])
data class DaoMovieGenreIdCrossRef(
    val genreId: Int,
    val movieId: Int
)
