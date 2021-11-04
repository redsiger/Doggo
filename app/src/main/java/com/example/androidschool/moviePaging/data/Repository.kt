package com.example.androidschool.moviePaging.data

import com.example.androidschool.moviePaging.util.Result.Status

abstract class Repository {

    companion object {
        private const val UNAUTHORIZED = "Unauthorized"
        private const val NOT_FOUND = "Not found"
        const val  SOMETHING_WRONG = "Something wrong"

        fun <T> handleSuccess(data: T): Status<T> {
            return Status.Success(data)
        }

        fun <T> handleHttpException(code: Int): Status<T> {
            val exception = getErrorMessage(code)
            return Status.Error(Exception(exception))
        }

        fun <T> handleException(exception: java.lang.Exception): Status<T> {
            return Status.Error(Exception(exception.message))
        }

        private fun getErrorMessage(code: Int): String {
            return when (code) {
                401 -> UNAUTHORIZED
                404 -> NOT_FOUND
                else -> SOMETHING_WRONG
            }
        }
    }
}