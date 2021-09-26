package com.example.androidschool.moviePaging.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MovieSearchResponses")
data class DaoMovieSearchResponse (

    @PrimaryKey(autoGenerate = false)
    val page: Int,
    val totalPages: Int,
    val totalResults: Int
)