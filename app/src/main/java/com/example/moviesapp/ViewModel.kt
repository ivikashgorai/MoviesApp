package com.example.moviesapp

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
//import com.example.moviesapp.MovieClass
import retrofit2.Response

class ViewModel(
    private val repository: Repository
) : ViewModel() {

    val getPopularMovies = MutableStateFlow<List<TopMoviesItem>>(emptyList())

    init{
        getPopularMovies()
    }
    fun getPopularMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val movies = repository.getPopularMovies()
                getPopularMovies.value = movies
//            Log.i("Movies23",movies.toString())
        }
    }
}
