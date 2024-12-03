package com.example.moviesapp

import com.example.moviesapp.Retrofit.RetrofitBuilder


class Repository {
    private val apiServices = RetrofitBuilder.getApi

    suspend fun getPopularMovies() : List<TopMoviesItem> {
        return apiServices.getPopularMovies(
            api_key  = "d9358b8fc5msh8f38a46215dad90p1da515jsn48c1a158ac79",
            host = "imdb-top-100-movies.p.rapidapi.com"
        )
    }

}