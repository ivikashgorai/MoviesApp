package com.example.moviesapp

import com.example.moviesapp.Retrofit.RetrofitBuilder
//import okhttp3.Response

//import com.example.moviesapp.Retrofit.RetrofitBuilder
//import com.example.moviesapp.data.
import retrofit2.Response

class Repository {
    private val apiServices = RetrofitBuilder.getApi

    suspend fun getPopularMovies() : List<TopMoviesItem> {
        return apiServices.getPopularMovies(
            api_key  = "8d1b32baa7msh19f98cbae135b25p106853jsnde9baf15d6fd",
            host = "imdb-top-100-movies.p.rapidapi.com"
        )
    }

}