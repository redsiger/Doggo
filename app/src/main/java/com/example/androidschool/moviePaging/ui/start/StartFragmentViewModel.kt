package com.example.androidschool.moviePaging.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartFragmentViewModel(): ViewModel() {

    @Inject
    lateinit var movieApi: MovieService

    var recyclerListData: MutableLiveData<List<Movie>> = MutableLiveData()

    init {
        App.appComponent.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            getPopularMovies()
        }
    }

    suspend fun getPopularMovies() {
        val response = movieApi.getPopularMovies()
        if (response.isSuccessful) {
            val movies = response.body()?.results
            recyclerListData.postValue(movies)
        }
    }
}