package com.example.androidschool.moviePaging.data.dao.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.androidschool.moviePaging.data.dao.DaoMovieSearchResponseWithMovies

@Dao
interface MovieSearchResponseDao {

    @Transaction
    @Query("SELECT * FROM MovieSearchResponses WHERE page=:query")
    suspend fun getPopularMovies(query: String = "1") : LiveData<DaoMovieSearchResponseWithMovies>
}