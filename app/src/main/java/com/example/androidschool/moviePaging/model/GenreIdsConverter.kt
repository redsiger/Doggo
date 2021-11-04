package com.example.androidschool.moviePaging.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class GenreIdsConverter {
    @TypeConverter
    fun fromGenres(genreIds: List<Int>): String {
        return Gson().toJson(genreIds)
    }

    @TypeConverter
    fun toGenres(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>(){}.type
        return Gson().fromJson(value, type)
    }
}