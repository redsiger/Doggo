package com.example.androidschool.moviePaging.ui.popularMovies

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_QUERY = "1"
private const val LAST_SEARCH_QUERY = "last_search_query"

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    val repository: MovieRepository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _popularMovies = MutableLiveData<PagingData<Movie>>()
    var isMoviesLoaded = MutableLiveData<Boolean>(false)
    private var searchResults = MutableLiveData<PagingData<Movie>>()
    var searchQuery: String = savedStateHandle.get<String>("searchQuery").toString()
    var searchQueryObservable = MutableLiveData<String>(searchQuery)

    init {
//        searchResult()
//        Log.e("PopularMoviesViewModel", searchQuery)
    }

    suspend fun getPopularMovies(): LiveData<PagingData<Movie>> {
        val response = repository.getPopularMoviesPaging().cachedIn(viewModelScope)
        _popularMovies.value = response.value
        isMoviesLoaded.value = true
        return response
    }

//    fun popularMovies(): Flow<PagingData<Movie>> {
//        return repository.getPopularMovies()
//            .cachedIn(viewModelScope)
//    }


    fun setQuery(query: String) {
        searchQueryObservable.postValue(query)
        Log.e("searchViewModel", "query posted")
    }

    fun searchResult(): LiveData<PagingData<Movie>> {
        val response = repository.getSearchResultPaging(searchQueryObservable.value!!).cachedIn(viewModelScope)
        searchResults.value = response.value
        return response
    }

    companion object {
        private const val DEFAULT_QUERY = "1"
    }
}