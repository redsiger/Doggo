package com.example.androidschool.moviePaging.data.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class  DaoMovieWithGenreIds (

    @Embedded
    val movie: DaoMovie,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(DaoMovieGenreIdCrossRef::class)
    )
    val genreIds: List<DaoMovieGenreId>
)