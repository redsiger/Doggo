package com.example.androidschool.moviePaging.util

import com.example.androidschool.moviePaging.model.Movie

interface MovieAdapter {
    fun setList(list: List<Movie>)
}