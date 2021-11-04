package com.example.androidschool.moviePaging.ui.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor (
    val alarmsDao: AlarmsDao
    ): ViewModel() {


    var alarms: MutableLiveData<List<Alarm>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            getAlarms()
        }
    }

    suspend fun getAlarms() {
//        viewModelScope.launch(Dispatchers.Default) {
            alarms.postValue(alarmsDao.getAlarms())
//        }
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmsDao.addAlarm(alarm)
        }
    }

    fun deleteAlarm(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmsDao.deleteAlarm(movieId)
        }
    }
}