package com.example.androidschool.moviePaging.ui.topRated

import androidx.lifecycle.MutableLiveData
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
class TopRatedViewModel @Inject constructor (
    val movieRepository: MovieRepository
        ): ViewModel() {

    val topRatedState = MutableLiveData<Status<List<Movie>?>>()
    private var topRatedList = mutableListOf<Movie>()
    private var currentPage = 1

    init {
        loadTopRated()
    }

    fun loadTopRated() {
        if (currentPage == 1) {
            topRatedState.postValue(Status.InProgress)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieRepository.getTopRatedMovies(currentPage)
            when (response) {
                is Status.Success -> {
                    val loadedList = response.extractData
                    if (currentPage == 1) {
                        topRatedList.addAll(loadedList as List<Movie>)
                        topRatedState.postValue(Status.Success(topRatedList))
                    } else {
                        val currentList: List<Movie>? = topRatedState.value?.extractData
                        if (currentList == null || currentList.isEmpty()) {
                            topRatedList.addAll(loadedList as List<Movie>)
                            topRatedState.postValue(Status.Success(topRatedList))
                        } else {
                            topRatedList.addAll(loadedList as List<Movie>)
                            topRatedState.postValue(Status.Success(topRatedList))
                        }
                    }
                }
                else -> topRatedState.postValue(response)
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        loadTopRated()
    }
}