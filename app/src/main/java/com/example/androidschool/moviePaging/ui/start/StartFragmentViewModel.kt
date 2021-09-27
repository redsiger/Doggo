package com.example.androidschool.moviePaging.ui.start

import android.app.Application
import androidx.lifecycle.*
import com.example.androidschool.moviePaging.data.dao.DaoMovieSearchResponse
import com.example.androidschool.moviePaging.data.dao.database.MovieSearchResponseDao
import com.example.androidschool.moviePaging.data.dao.database.MovieSearchResponseDatabase
import com.example.androidschool.moviePaging.di.APPLICATION_CONTEXT
import com.example.androidschool.moviePaging.di.App
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse
import com.example.androidschool.moviePaging.network.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartFragmentViewModel(application: Application): AndroidViewModel(application) {
//class StartFragmentViewModel(): ViewModel() {

    @Inject
    lateinit var movieApi: MovieService

//    private var movieDao: MovieSearchResponseDao = MovieSearchResponseDatabase
//        .getInstance(APPLICATION_CONTEXT).movieDao
    var movieResponse: MutableLiveData<MovieSearchResponse> = MutableLiveData()

    var recyclerListData: MutableLiveData<List<Movie>> = MutableLiveData()

    init {
        App.appComponent.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            getPopularMovies()
//            storeMovieResponseInRoom(movieResponse.value!!)
        }
    }

    suspend fun getPopularMovies() {
        val response = movieApi.getPopularMovies()
        if (response.isSuccessful) {
            val movies = response.body()?.results
            recyclerListData.postValue(movies)
            movieResponse.postValue(response.body())
        }
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