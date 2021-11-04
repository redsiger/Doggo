package com.example.androidschool.moviePaging.data.room.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "movieSearchResponses",
    primaryKeys = ["page", "section"])
data class MovieSearchResponseWithoutMovies(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val section: String
)