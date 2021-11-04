package com.example.androidschool.moviePaging.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.util.Result.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    val movieRepository: MovieRepository,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    var query: String = savedStateHandle.get<String>("searchQuery").toString()
    val searchResultState = MutableLiveData<Status<List<Movie>?>>()
    private var searchResultList = mutableListOf<Movie>()
    private var currentPage = 1

    init {
        getSearchResult()
    }

    fun getSearchResult() {
        if (currentPage == 1) {
            searchResultState.postValue(Status.InProgress)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getSearchResult(query, currentPage)
            when (response) {
                is Status.Success -> {
                    val loadedList = response.extractData
                    if (currentPage == 1) {
                        searchResultList.addAll(loadedList as List<Movie>)
                        searchResultState.postValue(Status.Success(searchResultList))
                    } else {
                        val currentList: List<Movie>? = searchResultState.value?.extractData
                        if (currentList == null || currentList.isEmpty()) {
                            searchResultList.addAll(loadedList as List<Movie>)
                            searchResultState.postValue(Status.Success(searchResultList))
                        } else {
                            searchResultList.addAll(loadedList as List<Movie>)
                            searchResultState.postValue(Status.Success(searchResultList))
                        }
                    }
                }
                else -> searchResultState.postValue(response)
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        getSearchResult()
    }
}