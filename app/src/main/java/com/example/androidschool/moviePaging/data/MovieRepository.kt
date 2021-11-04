package com.example.androidschool.moviePaging.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.androidschool.moviePaging.data.room.cache.MovieSearchResponseDao
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.DEFAULT_PAGE_SIZE
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.network.MovieService
import com.example.androidschool.moviePaging.util.Result.Status
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val movieSearchResponseDao: MovieSearchResponseDao
    ): Repository() {

    companion object {
        const val GENERAL_ERROR_CODE = 499
    }

    lateinit var mMovieById: Status<MovieById?>

    suspend fun getSearchResult(query: String, page: Int = 1): Status<List<Movie>?> {
        var result: Status<List<Movie>?> = handleSuccess(listOf())
        try {
            val response = movieService.getSearchResult(query, page)
            response?.let {
                it.body()?.results.let { moviesResponse ->
                    result = handleSuccess(moviesResponse)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleHttpException(errorCode)
                        }
                    } else result = handleHttpException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            return handleHttpException(error.code())
        } catch (exception: Exception) {
            return handleException(exception)
        }
        return result
    }

    suspend fun getMovieBySectionResponse(page: Int = 1, section: String): Status<List<Movie>?> {
        var result: Status<List<Movie>?> = handleSuccess(listOf())
        try {
            val response = movieService.getMoviesBySection(section, page)
            response?.let {
                it.body()?.results.let { moviesResponse ->
                    result = handleSuccess(moviesResponse)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleHttpException(errorCode)
                        }
                    } else result = handleHttpException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            return handleHttpException(error.code())
        } catch (exception: Exception) {
            return handleException(exception)
        }
        return result
    }

    suspend fun getMovieById(id: Int): Status<MovieById?> {
        try {
            val response = movieService.getMovieById(id)
            response?.let {
                it.body()?.let { movieById ->
                    mMovieById = handleSuccess(movieById)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            mMovieById = handleHttpException(errorCode)
                        }
                    } else mMovieById = handleHttpException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            return handleHttpException(error.code())
        } catch (exception: Exception) {
            return handleException(exception)
        }
        return mMovieById
    }

    suspend fun getMovieCast(movieId: Int): Status<List<CastMember>?> {
        var result: Status<List<CastMember>?> = handleSuccess(listOf())
        try {
            val response = movieService.getMovieCast(movieId)
            response?.let {
                it.body()?.cast.let { credits ->
                    result = handleSuccess(credits)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleHttpException(errorCode)
                        }
                    } else result = handleHttpException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            return handleHttpException(error.code())
        } catch (exception: Exception) {
            return handleException(exception)
        }
        return result
    }

    suspend fun getTrendingMovies(): Status<List<Movie>?> {
        var mediaType = "movie"
        var timeWindow = "week"
        var result: Status<List<Movie>?> = handleSuccess(listOf())
        try {
            val response = movieService.getTrending(mediaType, timeWindow)
            response?.let {
                it.body()?.results.let { moviesResponse ->
                    result = handleSuccess(moviesResponse)
                }
                it.errorBody()?.let { responseErrorBody ->
                    if (responseErrorBody is HttpException) {
                        responseErrorBody.response()?.code()?.let { errorCode ->
                            result = handleHttpException(errorCode)
                        }
                    } else result = handleHttpException(GENERAL_ERROR_CODE)
                }
            }
        } catch (error: HttpException) {
            return handleHttpException(error.code())
        } catch (exception: Exception) {
            return handleException(exception)
        }
        return result
    }

    suspend fun getPopularMovies(page: Int = 1, section: String = "popular"): Status<List<Movie>?> {
        return getMovieBySectionResponse(page, section)
    }

    suspend fun getTopRatedMovies(page: Int = 1, section: String = "top_rated"): Status<List<Movie>?> {
        return getMovieBySectionResponse(page, section)
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

    fun getSearchResultPaging(query: String, page: String = "1"): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchResultPagingSource(movieService, query) }
        ).liveData
    }
}