package com.example.androidschool.moviePaging.di

import com.example.androidschool.moviePaging.data.MovieRepository
import com.example.androidschool.moviePaging.network.MovieService
import com.example.androidschool.moviePaging.ui.popularMovies.MovieDetailsViewModel
import com.example.androidschool.moviePaging.ui.popularMovies.PopularMoviesViewModel
import com.example.androidschool.moviePaging.ui.start.StartFragmentViewModel
import com.squareup.picasso.Picasso
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MovieServiceModule::class, MovieRepositoryModule::class])
interface AppComponent {

    fun inject(movieRepository: MovieRepository)
    fun inject(popularMoviesViewModel: StartFragmentViewModel)
    fun inject(popularMoviesViewModel: PopularMoviesViewModel)
    fun inject(movieDetailsViewModel: MovieDetailsViewModel)
}