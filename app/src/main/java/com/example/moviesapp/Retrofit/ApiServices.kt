package com.example.moviesapp.Retrofit

//import com.example.moviesapp.MovieClass
//import com.example.moviesapp.MovieClass
//import com.example.moviesapp.TmdbResponse
import com.example.moviesapp.TopMoviesItem
//import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.Response

interface ApiServices {
    @GET("/")
    suspend fun getPopularMovies(
    @Header("x-rapidapi-key") api_key:String,
    @Header("x-rapidapi-host") host : String
    ): List<TopMoviesItem>
}

//.addHeader("x-rapidapi-key", "dfdccbe70cmsh6409a3801565efep1743a7jsn8632e426a237")
//	.addHeader("x-rapidapi-host", "moviesverse1.p.rapidapi.com")
//above header is provided in api call