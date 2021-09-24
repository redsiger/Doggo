package com.example.androidschool.moviePaging.notifications

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.androidschool.moviePaging.R
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(requireActivity(),this,  currentHour, currentMinutes, true)
    }

    override fun onTimeSet(view: TimePicker?, selectedHour: Int, selectedMinute: Int) {
        calendar.set(Calendar.HOUR, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val selectedDate = calendar.time.toString()

        val selectedDateBundle = Bundle()
        selectedDateBundle.putString("SELECTED_DATE", selectedHour.toString())

        setFragmentResult("REQUEST_KEY", selectedDateBundle)
    }

}