package com.example.moviesapp

import android.R.attr.text
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviesapp.ui.theme.MoviesAppTheme
import kotlin.getValue
//import com.example.moviesapp.TmdbResponse
import android.R.attr.text

//Api Key -> 8d1b32baa7msh19f98cbae135b25p106853jsnde9baf15d6fd
class MainActivity : ComponentActivity() { // App is not running
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository by lazy {
                Repository()
            }
            val ViewModel: ViewModel by viewModels {
                ViewModalFactory(repository)
            }
            val movie = ViewModel.getPopularMovies.collectAsState()
//            Log.i("Movies23", movie.value?.results?.size.toString())
            val list : List<TopMoviesItem> = movie.value
            if(list.isEmpty()){
                Text(text = "Loading")
            }else {
                Text(text = list[0].title)
            }

        }
    }
}

