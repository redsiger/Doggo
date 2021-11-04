package com.example.androidschool.moviePaging.network

import com.example.androidschool.moviePaging.network.Credits.CreditsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.UnknownHostException


const val DEFAULT_PAGE_INDEX = 1
const val DEFAULT_PAGE_SIZE = 20


interface MovieService {

    //    https://api.themoviedb.org/3/trending/movie/week?api_key=bfe801649ca860d496a1b7a533405418&language=ru
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(@Path("media_type") media_type: String, @Path("time_window") time_window: String) : Response<MovieSearchResponse>

    @Throws(UnknownHostException::class)
    @GET("movie/{section}")
    suspend fun getMoviesBySection(@Path("section") section: String, @Query("page") page: Int = 1): Response<MovieSearchResponse>

    //    https://api.themoviedb.org/3/movie/popular?api_key=bfe801649ca860d496a1b7a533405418
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): Response<MovieSearchResponse>?

    //    https://api.themoviedb.org/3/movie/top_rated?api_key=bfe801649ca860d496a1b7a533405418
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int = 1): Response<MovieSearchResponse>?

    @GET("search/movie")
    suspend fun getSearchResult(
        @Query("query") searchQuery: String,
        @Query("page") page: Int
    ) : Response<MovieSearchResponse>?

    @GET("movie/{id}")
    suspend fun getMovieById(@Path ("id") id: Int): Response<MovieById>?

    @GET("movie/{id}/credits")
    suspend fun getMovieCast(@Path("id") id: Int): Response<CreditsResponse>

//    companion object {
//        operator fun invoke() : MovieService {
//
//            val httpLoggingInterceptor = HttpLoggingInterceptor()
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//            val addApiKeyInterceptor = Interceptor {
//                val url = it.request()
//                    .url
//                    .newBuilder()
//                    .addQueryParameter("api_key", API_KEY)
//                    .build()
//                val request = it.request()
//                    .newBuilder()
//                    .url(url)
//                    .build()
//                return@Interceptor it.proceed(request)
//            }
//
//            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(addApiKeyInterceptor)
//                .addInterceptor(httpLoggingInterceptor)
//                .build()
//
//            val retrofit = Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(MovieService::class.java)
//
//            return retrofit
//        }
//    }
}