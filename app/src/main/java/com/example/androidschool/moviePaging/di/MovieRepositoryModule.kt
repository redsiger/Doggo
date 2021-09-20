package com.example.androidschool.moviePaging.di

import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.network.MovieService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MovieRepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(): MovieRepository {
        return MovieRepository()
    }
}