package com.example.androidschool.moviePaging.di

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.AlarmsDao
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideAppNotification(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager,
        notificationManagerCompat: NotificationManagerCompat,
        alarmsDao: AlarmsDao
    ): AppNotification {
        return AppNotification(
            context,
            alarmManager,
            notificationManagerCompat,
            alarmsDao
        )
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    @Named("NOTIFICATION_ID")
    fun provideNotificationId(): Int = 1

    @Provides
    fun provideTimeAndDate(): Calendar {
        return Calendar.getInstance(TimeZone.getDefault())
    }

//    @Provides
//    fun provideDatePicker(): MaterialDatePicker<Long> {
//        val calendar = Calendar.getInstance(TimeZone.getDefault())
////        val today = MaterialDatePicker.todayInUtcMilliseconds()
//        val today = Calendar.getInstance(TimeZone.getDefault()).timeInMillis
//
//        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
//        calendar[Calendar.MONTH] = Calendar.OCTOBER
//        val octThisYear = calendar.timeInMillis
//
//        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
//        calendar[Calendar.MONTH] = Calendar.NOVEMBER
//        val novThisYear = calendar.timeInMillis
//
//        val constraints =
//            CalendarConstraints.Builder()
//                .setStart(today)
//                .setValidator(DateValidatorPointForward.now())
//                .build()
//
//        return MaterialDatePicker.Builder.datePicker()
//            .setTitleText(R.string.datePicker)
//            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//            .setCalendarConstraints(constraints)
//            .build()
//    }

    @Provides
    fun provideDatePicker(): MaterialDatePicker.Builder<Long> {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
//        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val today = Calendar.getInstance(TimeZone.getDefault()).timeInMillis

        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        calendar[Calendar.MONTH] = Calendar.OCTOBER
        val octThisYear = calendar.timeInMillis

        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        calendar[Calendar.MONTH] = Calendar.NOVEMBER
        val novThisYear = calendar.timeInMillis

        val constraints =
            CalendarConstraints.Builder()
                .setStart(today)
                .setValidator(DateValidatorPointForward.now())
                .build()

        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.datePicker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
    }

    @Provides
    fun provideTimePicker(): MaterialTimePicker.Builder {
        val calendar = Calendar.getInstance(TimeZone.getDefault())

        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText(R.string.timePicker)
    }
}