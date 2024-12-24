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
import android.annotation.SuppressLint
import android.net.Uri
import android.view.Display
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil3.request.ImageRequest
import okhttp3.internal.filterList
import java.nio.file.WatchEvent

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
            MyApp(ViewModel)
        }
    }
}

@Composable
fun LoadingUI() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(), // Dark background for better contrast
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.Cyan, // Accent color for the loader
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between loader and text
            Text(
                text = "Loading...",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MyApp(viewModel: ViewModel) {
    val navController = rememberNavController()
    val movie = viewModel.getPopularMovies.collectAsState()
    val list : List<TopMoviesItem> = movie.value
    if(list.isEmpty()){
        LoadingUI()
    }
    else{
    Box(modifier = Modifier.background(Color.Black).fillMaxSize()) {// using box so to give background during transition otherwise it will give white flash while transition
        // Now define NavHost
        NavHost(
            navController = navController,
            startDestination = "Home"
        ){
            composable("Home"){
                MainScreenUi(viewModel,navController,list)
            }

            composable("search"){
                searchUi(viewModel,list)
            }
            composable("movie_information/{title}/{url}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("url") { type = NavType.StringType }
//                    navArgument("genre") { type = NavType.StringType },
                )){
                val navBackStackEntry = it
                val title = navBackStackEntry.arguments?.getString("title") ?:""
                val url = navBackStackEntry.arguments?.getString("url") ?:""
//                val genre = navBackStackEntry.arguments?.getString("genre") ?:""
                movieInfo(title,url)
            }
        }
    }
        }
}

@Composable
fun MainScreenUi(viewModel: ViewModel, navController: NavController,list: List<TopMoviesItem>){
        Box(modifier = Modifier.background(Color.Black).fillMaxSize()) {
            GridMovieLayout(list,navController)
            FloatingActionButton(onClick = {
                navController.navigate("search")
            },  containerColor = Color(135, 140, 148),
                modifier = Modifier.padding(end = 35.dp, bottom = 80.dp).align(Alignment.BottomEnd)) {
                        Image(painter = painterResource(id = R.drawable.search2),
                            contentDescription = "search",
                            modifier = Modifier.size(40.dp))
            }
        }
}




@Composable
fun GridMovieLayout(list : List<TopMoviesItem>,navController: NavController){

    LazyVerticalGrid(columns = GridCells.Fixed(3), /*no of column*/
        modifier = Modifier.padding(top = 50.dp, start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
       items(list){ movie->
           MovieCard(movie, navController)
       }
    }
}



@Composable
fun MovieCard(movie: TopMoviesItem,navController: NavController){
    Card(
        elevation = CardDefaults.cardElevation(10.dp)
        , modifier = Modifier.clickable(onClick = {
            val encodedTitle = Uri.encode(movie.title)
            val encodedUrl = Uri.encode(movie.big_image)
            navController.navigate("movie_information/${encodedTitle}/${encodedUrl}")
        })
    ) {
        if(!movie.image.isEmpty()) {
            CoilImage(movie.image,false)
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun movieInfo(title:String,url:String){
    val decodeTitle = Uri.decode(title)
    val decodeUrl = Uri.decode(url)

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column {
                Box(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                    CoilImage(decodeUrl,true) // true means give whole width image
                }
                Text(
                    decodeTitle,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
}

@Composable
fun searchUi(viewModel: ViewModel,list: List<TopMoviesItem>){
    var search by remember{
        mutableStateOf("")
    }
    val searchList: List<TopMoviesItem> = list.filter {
        it.title.contains(search)
    }
        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
            },
            placeholder = { Text(text = "Search Contacts") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(color = Color(50, 50, 50)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                unfocusedPlaceholderColor = Color(200, 200, 200),
            ),
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.search2),
                    contentDescription = "search icon",
                    modifier = Modifier.size(25.dp)
                )
            }
        )

        Box(modifier = Modifier.padding(top = 130.dp)) {
            if (searchList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Movies Found",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn {
                    items(searchList) {
                        DesignSearch(it.image, it.title, it.rating)
                    }
                }
            }
        }
}



@Composable
fun DesignSearch(url:String,name:String,rating:String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image on the left
           Card {
               CoilImage(url,false)
           }
            // Text in the center of remaining space
            Box(
                modifier = Modifier
                    .weight(1f) // Occupy remaining space
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$name\nIMDB Rating: $rating",
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }

}


@Composable
fun CoilImage(url: String,isBig:Boolean) {
    val context = LocalContext.current
    if(isBig){
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        return
    }
    AsyncImage(
        model = url,
        contentDescription = null,
    )
}