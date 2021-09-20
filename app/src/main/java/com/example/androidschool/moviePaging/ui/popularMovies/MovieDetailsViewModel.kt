package com.example.androidschool.moviePaging.ui.popularMovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.MovieById
import com.example.androidschool.moviePaging.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel(
    val movieId: String
): ViewModel() {

    var movieById = MutableLiveData<MovieById>()
    var isMoviesLoaded = MutableLiveData<Boolean>(false)

    @Inject
    lateinit var apiService: MovieService

    init {
        App.appComponent.inject(this)
        getMovieById()
    }

    private fun getMovieById() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = apiService.getMovieById(movieId)
            if (response.isSuccessful) {
                val movieByIdResponse = response.body() as MovieById
                movieById.postValue(movieByIdResponse)
                isMoviesLoaded.postValue(true)
            }
        }
    }
}