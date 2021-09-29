package com.example.androidschool.moviePaging.ui.popularMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.network.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    val apiService: MovieService,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    var movieById = MutableLiveData<MovieById>()
    var isMoviesLoaded = MutableLiveData<Boolean>(false)
    var movieId: String = savedStateHandle.get<String>("MovieId").toString()

    init {
        getMovieById()
    }

    private fun getMovieById() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.getMovieById(movieId)
            if (response.isSuccessful) {
                val movieByIdResponse = response.body() as MovieById
                movieById.postValue(movieByIdResponse)
                isMoviesLoaded.postValue(true)
            }
        }
    }

//    private fun getMoviesCast() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = apiService.getMovieById(movieId)
//            if (response.isSuccessful) {
//                val movieByIdResponse = response.body() as MovieById
//                movieById.postValue(movieByIdResponse)
//                isMoviesLoaded.postValue(true)
//            }
//        }
//    }
}