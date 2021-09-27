package com.example.androidschool.moviePaging.data.dao.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidschool.moviePaging.data.dao.*

@Dao
interface MovieSearchResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieSearchResponse(daoMovieSearchResponse: DaoMovieSearchResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(daoMovie: DaoMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreId(daoMovieGenreId: DaoMovieGenreId)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDaoMovieGenreIdCrossRef(daoMovieGenreIdCrossRef: DaoMovieGenreIdCrossRef)

    @Transaction
    @Query("SELECT * FROM Movies WHERE movieId=:movieId")
    suspend fun getGenreIdsofMovie(movieId: Int) : List<DaoMovieWithGenreIds>

    @Transaction
    @Query("SELECT * FROM GenreIds WHERE genreId=:genreId")
    suspend fun getMoviesOfGenreId(genreId: Int) : List<DaoGenreIdWithMovies>

    @Transaction
    @Query("SELECT * FROM MovieSearchResponses WHERE page=:page")
    suspend fun getMovieSearchResponseWithMovies(page: String) : List<DaoMovieSearchResponseWithMovies>
}