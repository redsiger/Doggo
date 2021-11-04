package com.example.androidschool.moviePaging.ui.start

import androidx.lifecycle.*
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.androidschool.moviePaging.util.Result.Status

@HiltViewModel
class StartFragmentViewModel @Inject constructor(
    val movieRepository: MovieRepository
    ): ViewModel() {

    var movieResponse: MutableLiveData<MovieSearchResponse> = MutableLiveData()
    var isAllDataLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    var isPopularLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    var isTopRatedLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    var popularsListData: MutableLiveData<List<Movie>> = MutableLiveData()
    var topRatedListData: MutableLiveData<List<Movie>> = MutableLiveData()

    val trendingState = MutableLiveData<Status<List<Movie>>>()
    val popularState = MutableLiveData<Status<List<Movie>>>()
    val topRatedState = MutableLiveData<Status<List<Movie>>>()

    fun loadState() {
        trendingState.postValue(Status.InProgress)
        popularState.postValue(Status.InProgress)
        topRatedState.postValue(Status.InProgress)
        viewModelScope.launch(Dispatchers.IO) {
            trendingState.postValue(movieRepository.getTrendingMovies() as Status<List<Movie>>)
            popularState.postValue(movieRepository.getPopularMovies() as Status<List<Movie>>)
            topRatedState.postValue(movieRepository.getTopRatedMovies() as Status<List<Movie>>)
        }
    }

    init {
        loadState()
    }




//    fun getPopularMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            isPopularLoaded.postValue(false)
//            popularsListData.postValue(movieRepository.getPopularMovies())
//            isPopularLoaded.postValue(true)
//            isAllDataLoaded()
//        }
//    }
//
//    fun getTopRatedMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            isTopRatedLoaded.postValue(false)
//            topRatedListData.postValue(movieRepository.getTopRatedMovies())
//            isTopRatedLoaded.postValue(true)
//            isAllDataLoaded()
//        }
//    }

//    suspend fun storeMovieResponseInRoom(response: MovieSearchResponse) {
//        val daoMovieSearchResponse = DaoMovieSearchResponse(
//            response.page,
//            response.totalPages,
//            response.totalResults
//        )
//        movieDao.insertMovieSearchResponse(daoMovieSearchResponse)
//    }
}