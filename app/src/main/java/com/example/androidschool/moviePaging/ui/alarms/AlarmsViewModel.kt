package com.example.androidschool.moviePaging.ui.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidschool.moviePaging.data.room.Alarm
import com.example.androidschool.moviePaging.data.room.AlarmsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor (
    val alarmsDao: AlarmsDao
    ): ViewModel() {

    var alarms: MutableLiveData<List<Alarm>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAlarms()
        }
    }

    suspend fun getAlarms() {
        alarms.postValue(alarmsDao.getAlarms())
    }
}