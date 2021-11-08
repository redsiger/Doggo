package com.example.androidschool.moviePaging.network.videoresponse

import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("key")
    val key: String
) {
    constructor(): this(key = "")
}
