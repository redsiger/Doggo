package com.example.androidschool.moviePaging.util.Result

import java.lang.Exception

sealed class Status<out T> {
    data class Success<out T>(val data: T): Status<T>()
    data class Error(val exception: Exception): Status<Nothing>()
    object InProgress: Status<Nothing>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
        }
}