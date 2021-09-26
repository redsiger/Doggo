package com.example.androidschool.moviePaging.data.dao


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class DaoMovie(
    val searchResponsePage: Int,

    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val adult: Boolean,
    val backdropPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Double
)