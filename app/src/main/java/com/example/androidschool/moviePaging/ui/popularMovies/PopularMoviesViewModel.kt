package com.example.androidschool.moviePaging.ui.popularMovies

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.model.Movie
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_QUERY = "1"
private const val LAST_SEARCH_QUERY = "last_search_query"

class PopularMoviesViewModel(
//    private val repository: MovieRepository
): ViewModel() {

    private val repository = MovieRepository()

    private val _popularMovies = MutableLiveData<PagingData<Movie>>()

    suspend fun getPopularMovies(): LiveData<PagingData<Movie>> {
        val response = repository.getPopularMovies().cachedIn(viewModelScope)
        _popularMovies.value = response.value
        return response
    }

//    fun popularMovies(): Flow<PagingData<Movie>> {
//        return repository.getPopularMovies()
//            .cachedIn(viewModelScope)
//    }

    fun searchResult(query: String): Flow<PagingData<Movie>> {
        return repository.getSearchResult(query)
            .cachedIn(viewModelScope)
    }

    companion object {
        private const val DEFAULT_QUERY = "1"
    }
}