package com.marklynch.steamdeck.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marklynch.steamdeck.ui.theme.SteamDeckTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import com.marklynch.steamdeck.data.image.disk.ImageData
import com.marklynch.steamdeck.data.image.disk.ImageRepository
import com.marklynch.steamdeck.data.image.disk.ImageRepositoryImpl
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

val photos:Int = 20
val gridSize:Int = 128
val colors: Array<Color> = arrayOf(
    Color(0xFF3D348B),// Dark Blue
    Color(0xFF7678ED),// Light Blue
    Color(0xFFF7B801), // Yellow
    Color(0xFFF18701),  // Light Orange
    Color(0xFFF35B04), // Dark Orange
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageRepository = ImageRepositoryImpl(this)
        setContent {
            App()
//            SteamDeckTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Grid(Modifier.padding(innerPadding), imageRepository)
//                }
//            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("detail") { DetailScreen(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")
        Button(onClick = { navController.navigate("detail") }) {
            Text("Go to Detail Screen")
        }
    }
}

@Composable
fun DetailScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Detail Screen")
        Button(onClick = { navController.navigateUp() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun Grid(modifier: Modifier, imageRepository: ImageRepository) {
    //Grid
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = gridSize.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(photos) { photo ->
            PhotoItem(photo)
        }
    }

    //Images
    val imageViewModel = ImageViewModel(imageRepository)
    ImageListScreen(imageViewModel)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SteamDeckTheme {
        val context = LocalContext.current
        Grid(Modifier, ImageRepositoryImpl(context))
    }
}

@Composable
fun PhotoItem(photo: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = colors[photo% colors.size],
                shape = RoundedCornerShape(16.dp) )
    )
}

@Composable
fun ImageListScreen(viewModel: ImageViewModel) {
    val images by viewModel.images.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    LazyColumn {
        items(images) { imageData ->
            ImageItem(imageData)
        }
    }
}

@Composable
fun ImageItem(imageData: ImageData) {
    Row(modifier = Modifier.padding(8.dp)) {
        // Load image using Coil (or other libraries) from Uri
        Image(
            painter = rememberAsyncImagePainter(model = imageData.uri),
            contentDescription = imageData.name,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = imageData.name)
            Text(text = "Date Taken: ${imageData.dateTaken}")
        }
    }
}



