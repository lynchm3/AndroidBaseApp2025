package com.marklynch.steamdeck.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.marklynch.steamdeck.BuildConfig
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.data.image.resources.ImageDataFromResourcesRepositoryImpl
import timber.log.Timber
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


val gridItems:Int = 20
val gridItemWith:Int = 128
val colors: Array<Color> = arrayOf(
    Color(0xFF3D348B),// Dark Blue
    Color(0xFF7678ED),// Light Blue
    Color(0xFFF7B801), // Yellow
    Color(0xFFF18701),  // Light Orange
    Color(0xFFF35B04), // Dark Orange
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
//        else {
//            plant(CrashReportingTree())
//        }
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        val imageRepository = ImageDataFromResourcesRepositoryImpl(this)
        setContent {
            App(imageRepository)
//            SteamDeckTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Grid(Modifier.padding(innerPadding), imageRepository)
//                }
//            }
        }
    }
}

//class CrashReportingTree : Tree() {
//    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
//            return
//        }
//
//        FakeCrashLibrary.log(priority, tag, message)
//
//        if (t != null) {
//            if (priority == Log.ERROR) {
//                FakeCrashLibrary.logError(t)
//            } else if (priority == Log.WARN) {
//                FakeCrashLibrary.logWarning(t)
//            }
//        }
//    }
//}

@Composable
fun App(imageRepository: ImageRepository) {
    val navController = rememberNavController()
    val imageViewModel = ImageViewModel(imageRepository)
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, imageRepository) }
        composable("add") { AddScreen(navController) }
        composable("images") { ImageListScreen(imageViewModel) }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val imageRepository = ImageDataFromResourcesRepositoryImpl(context)
    val imageViewModel = ImageViewModel(imageRepository)
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, ImageDataFromResourcesRepositoryImpl(context)) }
        composable("add") { AddScreen(navController) }
        composable("images") { ImageListScreen(imageViewModel) }
    }
}

@Composable
fun HomeScreen(navController: NavController, imageRepository: ImageRepository) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("images") }) {
            Text("+ Add")
        }
        Grid(imageRepository)
    }
}

@Composable
fun AddScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Screen")
    }
}

@Composable
fun Grid(imageRepository: ImageRepository) {
    //Grid
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = gridItemWith.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gridItems) { index ->
            GridItem(index)
        }
    }

    //Images
    val imageViewModel = ImageViewModel(imageRepository)
    ImageListScreen(imageViewModel)
}

@Composable
fun GridItem(photo: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = colors[photo % colors.size],
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@Composable
fun ImageListScreen(viewModel: ImageViewModel) {

    Timber.d("ImageListScreen")
    val images by viewModel.images.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    LazyColumn {
        itemsIndexed(images) { index, imageData ->
            ImageItem(imageData, colors[index % colors.size])
        }
    }
}

@Composable
fun ImageItem(imageData: ImageData, backgroundColor: Color) {
    Row(modifier = Modifier.padding(8.dp)) {
        // Load image using Coil (or other libraries) from Uri
        Image(
            painter = rememberAsyncImagePainter(model = imageData.uri),
            contentDescription = "",
            modifier = Modifier.size(80.dp).background(backgroundColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "IMAGE NAME")
            Text(text = "IMAGE DATE")
        }
    }
}



