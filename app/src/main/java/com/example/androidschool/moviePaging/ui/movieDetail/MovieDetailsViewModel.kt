package com.example.androidschool.moviePaging.ui.movieDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.Genre
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.network.ProductionCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.androidschool.moviePaging.util.Result.Status
import kotlinx.coroutines.delay

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    val movieRepository: MovieRepository,
    val alarmsDao: AlarmsDao,
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    var movieById = MutableLiveData<MovieById>()
    var movieByIdCredits = MutableLiveData<List<CastMember>>()
//    var movieId: String = savedStateHandle.get<String>("MovieId").toString()

    var movieState = MutableLiveData<Status<MovieById>>()
    var genres = MutableLiveData<String>("-")
    var countries = MutableLiveData<String>("-")

    var castState = MutableLiveData<Status<List<CastMember>>>()

    val state = MutableLiveData<Status<MovieDetailViewState>>(Status.Success(MovieDetailViewState()))

//    init {
//        getMovieById()
//    }

    fun postMovie(movieById: MovieById) {
        val currentState = state.value!!.extractData
            if (!currentState!!.isMovieReady) {
                val genres = prepareGenres(movieById.genres)
                val countries = prepareCountries(movieById.productionCountries)
                val newState = currentState.copy(movieById = movieById, isMovieReady = true, genres = genres, countries = countries)
                state.postValue(Status.Success(newState))
                Log.e("movie", "movie posted")
            }
    }

    fun postCast(castList: List<CastMember>) {
        val currentState = state.value!!.extractData
        if (!currentState!!.isCastReady) {
            val newState = currentState.copy(castList = castList, isCastReady = true)
            state.postValue(Status.Success(newState))
            Log.e("castList", "castList posted")
        }
    }

    fun isStateReady() {
        val currentState = state.value!!.extractData
        if (currentState!!.isMovieReady && currentState!!.isCastReady) {
            val newState = currentState.copy(isStateReady = true)
            state.postValue(Status.Success(newState))
            Log.e("state", "state posted")
        } else {
            Log.e("state", "state NOT posted")
        }
    }

    fun addAlarm(mMovieById: MovieById, time: Long) {
        viewModelScope.launch {
            val alarm = Alarm(
                mMovieById.id,
                mMovieById.title,
                time
            )
            alarmsDao.addAlarm(alarm)
        }
    }

    fun cancelAlarm(movieId: Int) {
        viewModelScope.launch {
            alarmsDao.deleteAlarm(movieId)
        }
    }

    private fun prepareGenres(genreList: List<Genre>): String {
        var result = "-"
        if (!genreList.isNullOrEmpty()) {
            result = ""
            genreList.forEach { g ->
                if (g != genreList.last()) {
                    result = result + g.name + ", "
                } else {
                    result += g.name
                }
            }
        }
        return result
    }

    private fun prepareCountries(countryList: List<ProductionCountry>): String {
        var result = "-"
        if (!countryList.isNullOrEmpty()) {
            result = ""
            countryList.forEach { c ->
                if (c != countryList.last()) {
                    result = result + c.name + ", "
                } else {
                    result += c.name
                }
            }
        }
        return result
    }

    fun getMovieById(movieId: Int) {
        movieState.postValue(Status.InProgress)
        viewModelScope.launch(Dispatchers.IO) {
            var movieById = movieRepository.getMovieById(movieId) as Status<MovieById>
            if (movieById is Status.Success) {
                genres.postValue(prepareGenres(movieById.data.genres))
                countries.postValue(prepareCountries(movieById.data.productionCountries))
            }
            movieState.postValue(movieById)
            getMoviesCast(movieId)
        }
    }

    private suspend fun getMoviesCast(movieId: Int) {
        val cast = movieRepository.getMovieCast(movieId) as Status<List<CastMember>>?
        castState.postValue(cast!!)
    }

//    private fun getMoviesCast() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = apiService.getMovieCast(movieId)
//            if (response!!.isSuccessful) {
//                val movieCastResponse = response.body()?.cast
//                movieByIdCredits.postValue(movieCastResponse!!)
//            }
//        }
//    }

//    private fun getMovieById() {
//        viewModelScope.launch {
//            val response = apiService.getMovieById(movieId)
//            if (response!!.isSuccessful) {
//                val movieByIdResponse = response.body() as MovieById
//                movieById.postValue(movieByIdResponse)
//                isMoviesLoaded.postValue(true)
//            }
//        }
//    }
}