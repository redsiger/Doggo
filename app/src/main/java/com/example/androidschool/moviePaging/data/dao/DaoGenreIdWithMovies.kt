package com.example.androidschool.moviePaging.data.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DaoGenreIdWithMovies(

    @Embedded
    val genreId: DaoMovieGenreId,

    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(DaoMovieGenreIdCrossRef::class)
    )
    val movies: List<DaoMovie>
)
