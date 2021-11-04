package com.example.androidschool.moviePaging.data.room.cache

import android.util.Log
import androidx.room.*
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Dao
interface MovieSearchResponseDao {

    @Transaction
    @Query("SELECT * FROM movieSearchResponses")
    fun getCachedMovies(): List<MovieSearchResponseWithMovies>

    @Transaction
    @Query("SELECT * FROM movieSearchResponses WHERE page = :page AND section = :section")
    fun getCachedPage(page: Int, section: String): MovieSearchResponseWithMovies

    @Query("SELECT * FROM movieSearchResponses JOIN movies ON movieSearchResponses.page = :page AND movieSearchResponses.section =:section AND movieSearchResponses.page = movies.movieSearchResponsePage AND movieSearchResponses.section = movies.section")
    fun getCachedMap(page: Int, section: String): Map<MovieSearchResponseWithoutMovies, List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieSearchResponseWithoutMovies(movieSearchResponseWithoutMovies: MovieSearchResponseWithoutMovies)

    suspend fun cacheMovieSearchResponse(
        movieSearchResponse: MovieSearchResponse?,
        section: String) {
        val movies: List<Movie> = movieSearchResponse!!.results.map {
            it.movieSearchResponsePage = movieSearchResponse.page
            it.section = section
            return@map it
        }
        movies.forEach {
            Log.e("TryingToSave", it.title)
            insertMovies(it)
        }

        val movieSearchResponseWithoutMovies = MovieSearchResponseWithoutMovies(
            page = movieSearchResponse.page,
            totalPages = movieSearchResponse.totalPages,
            totalResults = movieSearchResponse.totalResults,
            section = section
        )
        insertMovieSearchResponseWithoutMovies(movieSearchResponseWithoutMovies)
    }
}