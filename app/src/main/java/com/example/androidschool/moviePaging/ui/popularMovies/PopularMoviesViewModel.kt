package com.example.androidschool.moviePaging.ui.popularMovies

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_QUERY = "1"
private const val LAST_SEARCH_QUERY = "last_search_query"

class PopularMoviesViewModel(
//    private val repository: MovieRepository
): ViewModel() {

    @Inject
    lateinit var repository: MovieRepository

    init {
        App.appComponent.inject(this)
    }

    private val _popularMovies = MutableLiveData<PagingData<Movie>>()
    var isMoviesLoaded = MutableLiveData<Boolean>(false)

    suspend fun getPopularMovies(): LiveData<PagingData<Movie>> {
        val response = repository.getPopularMovies().cachedIn(viewModelScope)
        _popularMovies.value = response.value
        isMoviesLoaded.value = true
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