package com.example.androidschool.moviePaging.network.Credits

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<CastMember>,
    @SerializedName("crew")
    val crew: List<CrewMember>
)
