package com.example.androidschool.moviePaging.ui.start

import android.app.Application
import androidx.lifecycle.*
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.di.APPLICATION_CONTEXT
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse
import com.example.androidschool.moviePaging.network.MovieService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartFragmentViewModel @Inject constructor(
    val movieRepository: MovieRepository
    ): ViewModel() {

    var movieResponse: MutableLiveData<MovieSearchResponse> = MutableLiveData()
    var recyclerListData: MutableLiveData<List<Movie>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPopularMovies()
//            storeMovieResponseInRoom(movieResponse.value!!)
        }
    }

    suspend fun getPopularMovies() {
        recyclerListData.postValue(movieRepository.getPopularMovies())
    }

//    suspend fun storeMovieResponseInRoom(response: MovieSearchResponse) {
//        val daoMovieSearchResponse = DaoMovieSearchResponse(
//            response.page,
//            response.totalPages,
//            response.totalResults
//        )
//        movieDao.insertMovieSearchResponse(daoMovieSearchResponse)
//    }
}