package com.marklynch.steamdeck.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.marklynch.steamdeck.BuildConfig
import com.marklynch.steamdeck.R
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.data.image.resources.ImageDataFromResourcesRepositoryImpl
import timber.log.Timber
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


//val gridItems:Int = 20
val gridItemWith:Int = 128
val colors: Array<Color> = arrayOf(
    Color(0xFF3D348B),// Dark Blue
    Color(0xFF7678ED),// Light Blue
    Color(0xFFF7B801), // Yellow
    Color(0xFFF18701),  // Light Orange
    Color(0xFFF35B04), // Dark Orange
)

val buttons:MutableList<StreamDeckButton> = mutableListOf(StreamDeckButton())

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }

        val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
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



    @Composable
    fun App(imageRepository: ImageRepository) {
        val navController = rememberNavController()
        val imageViewModel = ImageViewModel(imageRepository)
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(navController, imageRepository) }
            composable("add") { PickInteractionScreen(navController) }
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
            composable("add") { PickInteractionScreen(navController) }
        }
    }

    @Composable
    fun HomeScreen(navController: NavController, imageRepository: ImageRepository) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//        Button(onClick = { navController.navigate("images") }) {
//            Text("+ Add")
//        }
            Grid(navController)
        }
    }

    @Composable
    fun Grid(navController: NavController) {
        //Grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = gridItemWith.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier.verticalScroll(rememberScrollState(), enabled = false)

//            .verticalScroll(rememberScrollState())
        ) {
            items(buttons.size) { index ->
                GridItem(index, navController)
            }
        }
    }

    @Composable
    fun GridItem(buttonIndex: Int, navController: NavController) {
        val context = LocalContext.current
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            selectedImageUri = uri // Update the selected image URI
        }
        Image(
            painter = rememberAsyncImagePainter(R.drawable.icon_plus),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(
                    color = colors[buttonIndex % colors.size],
                    shape = RoundedCornerShape(16.dp)
                ).clickable {
//                    navController.navigate("add")
                    launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                }
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImageUri)
                    .size(Size.ORIGINAL) // Load original size
                    .build()
            ),
            contentDescription = "Selected Image",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(1f)
//            .background(
//                color = colors[buttonIndex % colors.size],
//                shape = RoundedCornerShape(16.dp)
//            )
//    ) {
//    }
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
}



