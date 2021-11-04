package com.example.androidschool.moviePaging.ui.alarms

import android.app.Dialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.databinding.FragmentAlarmDialog1Binding
import com.example.androidschool.moviePaging.databinding.FragmentAlarmDialogBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.notifications.AlarmReceiver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmDialogFragment: DialogFragment() {

    @Inject
    lateinit var mDateAndTime: Calendar
    @Inject
    lateinit var mDatePicker: MaterialDatePicker.Builder<Long>
    @Inject
    lateinit var mTimePicker: MaterialTimePicker.Builder
    lateinit var mTimeFormatter: SimpleDateFormat
    lateinit var mDateFormatter: SimpleDateFormat


    companion object {
        val TAG = AlarmDialogFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        val KEY_RESPONSE = "RESPONSE"
        val KEY_RESPONSE_TIME = "TIME"
        val KEY_RESPONSE_MOVIE_TITLE = "MOVIE_TITLE"
        val KEY_RESPONSE_MOVIE_ID = "MOVIE_ID"
        val KEY_RESPONSE_MOVIE_POSITION = "MOVIE_POSITION"
    }

//    private var _binding: FragmentAlarmDialogBinding? = null
    private var _binding: FragmentAlarmDialog1Binding? = null
    private val mBinding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val movieTitle = requireArguments().getString("movieTitle")
        val movieId = requireArguments().getInt("movieId")
        val time = requireArguments().getLong("time")
        val position = requireArguments().getInt("position")

        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY,
                bundleOf(
                KEY_RESPONSE to which,
                KEY_RESPONSE_TIME to mDateAndTime.timeInMillis,
                KEY_RESPONSE_MOVIE_TITLE to movieTitle,
                KEY_RESPONSE_MOVIE_ID to movieId,
                KEY_RESPONSE_MOVIE_POSITION to position))
        }

        _binding = FragmentAlarmDialog1Binding.inflate(layoutInflater)

        mDateAndTime.timeInMillis = time

        mTimeFormatter = SimpleDateFormat("HH:mm")
        mDateFormatter = SimpleDateFormat("dd-MMM-yyyy")


        updateUI()
        initDatePicker()
        initTimePicker()

        return AlertDialog.Builder(requireContext())
            .setTitle(movieTitle)
            .setView(mBinding.root)
            .setPositiveButton(R.string.save, listener)
            .setNegativeButton(R.string.cancel, listener)
            .create()
    }

    private fun initDatePicker() {
        val picker = mDatePicker
            .setSelection(mDateAndTime.timeInMillis)
            .build()
        picker.addOnPositiveButtonClickListener {
            val pickedDate = Calendar.getInstance(TimeZone.getTimeZone("Moscow"))
            pickedDate.time = Date(it)
            val pickedYear = pickedDate.get(Calendar.YEAR)
            val pickedMonth = pickedDate.get(Calendar.MONTH)
            val pickedDay = pickedDate.get(Calendar.DAY_OF_MONTH)
            mDateAndTime.set(Calendar.YEAR, pickedYear)
            mDateAndTime.set(Calendar.MONTH, pickedMonth)
            mDateAndTime.set(Calendar.DAY_OF_MONTH, pickedDay)
            updateUI()
        }

        val showPickerClickListener = View.OnClickListener {
            picker.show(childFragmentManager, "datePicker")
        }

        mBinding.alarmDialogDateContainer.setOnClickListener(showPickerClickListener)
    }

    private fun initTimePicker() {
        val picker = mTimePicker
            .setHour(mDateAndTime.get(Calendar.HOUR_OF_DAY))
            .setMinute(mDateAndTime.get(Calendar.MINUTE))
            .build()
        picker.addOnPositiveButtonClickListener {
            mDateAndTime.set(Calendar.MILLISECOND, 0)
            mDateAndTime.set(Calendar.SECOND, 0)
            mDateAndTime.set(Calendar.MINUTE, picker.minute)
            mDateAndTime.set(Calendar.HOUR_OF_DAY, picker.hour)
            updateUI()
        }

        val showPickerClickListener = View.OnClickListener {
            picker.show(childFragmentManager, "timePicker")
        }

        mBinding.alarmDialogTimeContainer.setOnClickListener(showPickerClickListener)
    }

    private fun updateUI() {
        mBinding.alarmDialogTimeValue.text = mTimeFormatter.format(Date(mDateAndTime.timeInMillis))
        mBinding.alarmDialogDateValue.text = mDateFormatter.format(Date(mDateAndTime.timeInMillis))
    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val listener = DialogInterface.OnClickListener { _, which ->
//            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
//            Toast.makeText(requireContext(), "Negative", Toast.LENGTH_SHORT).show()
//        }
//
//        _binding = FragmentAlarmDialogBinding.inflate(inflater, container, false)
//
//        val timeFormatter = SimpleDateFormat("HH:mm")
//        val dateFormatter = SimpleDateFormat("dd-MMM-yyyy")
//
//        val movieTitle = requireArguments().getString("movieTitle")
//        val time = requireArguments().getLong("time")
//
//        mBinding.alarmDialogMovieTitle.text = movieTitle
//        mBinding.alarmDialogTimeValue.text = timeFormatter.format(Date(time))
//        mBinding.alarmDialogDateValue.text = dateFormatter.format(Date(time))
//
//        mBinding.alarmDialogBtnNegative.setOnClickListener(listener)
//
//        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//
//        return mBinding.root
//    }
//
//    override fun onDismiss(dialog: DialogInterface) {
//        Toast.makeText(requireContext(), "onDismissClicked", Toast.LENGTH_SHORT).show()
//        super.onDismiss(dialog)
//    }
//
//    override fun onClick(btn: View?) {
//
//
//        btn as Button
//        Toast.makeText(requireContext(), btn.text, Toast.LENGTH_SHORT).show()
//        dismiss()
//    }
}