package com.example.androidschool.moviePaging.network.videoresponse

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results")
    val results: List<Trailer>
) {
    constructor(): this(
        emptyList()
    )
}