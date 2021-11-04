package com.example.androidschool.moviePaging.data.room.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse

data class MovieSearchResponseWithMovies(
    @Embedded
    val movieSearchResponse: MovieSearchResponseWithoutMovies,
    @Relation(
        parentColumn = "page",
        entityColumn = "movieSearchResponsePage"
    )
    val movies: List<Movie>
)