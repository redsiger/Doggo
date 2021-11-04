package com.example.androidschool.moviePaging.ui.movieDetail

import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.MovieById

data class MovieDetailViewState(
    val movieById: MovieById,
    val castList: List<CastMember> = emptyList(),
    val countries: String,
    val genres: String,
    val isMovieReady: Boolean,
    val isCastReady: Boolean,
    val isStateReady: Boolean
) {
    constructor(): this(
        movieById = MovieById(),
        castList = emptyList(),
        countries = "",
        genres = "",
        isMovieReady = false,
        isCastReady = false,
        isStateReady = false
        )
}
