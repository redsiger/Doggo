package com.example.androidschool.moviePaging.di

import com.example.androidschool.moviePaging.R
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.datePicker_title)
            .build()
    }
}