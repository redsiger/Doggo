package com.example.androidschool.moviePaging.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.DEFAULT_PAGE_SIZE
import com.example.androidschool.moviePaging.network.MovieService
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(val movieService: MovieService) {


//    companion object {
//        operator fun invoke(): MovieRepository {
//            return invoke()
//        }
//    }

//    fun getPopularMovies(query: String = "1"): Flow<PagingData<Movie>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = DEFAULT_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { MoviePagingSource(movieService, query) }
//        ).flow
//    }

    suspend fun getPopularMovies(): List<Movie>? {
        try {
            val response = movieService.getPopularMovies()
            if (response.isSuccessful) {
                return response.body()?.results
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return emptyList()
    }

    fun getPopularMoviesPaging(query: String = "1"): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieService, query) }
        ).liveData
    }

    fun getSearchResultPaging(query: String, page: String = "1"): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchResultPagingSource(movieService, query) }
        ).flow
    }
}