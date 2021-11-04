package com.example.androidschool.moviePaging.di

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PicassoModule {

    @Singleton
    @Provides
    fun providePicasso(): Picasso {
        return Picasso.get()
    }
}