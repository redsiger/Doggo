package com.example.androidschool.moviePaging.data.dao

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class DaoMovieSearchResponseWithMovies(

    @Embedded
    val movieSearchResponse: DaoMovieSearchResponse,

    @Relation(
        entity = DaoMovie::class,
        parentColumn = "page",
        entityColumn = "searchResponsePage"
    )

    val MoviesWithGenreIds: List<DaoMovieWithGenreIds>
)