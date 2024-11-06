package com.marklynch.steamdeck.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
import com.marklynch.steamdeck.data.buttons.StreamDeckButtons
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.data.image.resources.ImageDataFromResourcesRepositoryImpl
import timber.log.Timber
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


//val gridItems:Int = 20
val gridItemWith: Int = 128
val colors: Array<Color> = arrayOf(
    Color(0xFF3D348B),// Dark Blue
    Color(0xFF7678ED),// Light Blue
    Color(0xFFF7B801), // Yellow
    Color(0xFFF18701),  // Light Orange
    Color(0xFFF35B04), // Dark Orange
)

val buttons: MutableList<StreamDeckButton> = mutableListOf(
    StreamDeckButton(),
    StreamDeckButton(),
    StreamDeckButton(),
    StreamDeckButton(),
    StreamDeckButton(),
    StreamDeckButton()
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        val imageRepository = ImageDataFromResourcesRepositoryImpl(this)
        setContent {
            App(imageRepository)
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
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    navController,
                    ImageDataFromResourcesRepositoryImpl(context)
                )
            }
            composable("add") { PickInteractionScreen(navController) }
        }
    }

    @Composable
    fun HomeScreen(
        navController: NavController,
        imageRepository: ImageRepository
    ) {
        var editMode by remember { mutableStateOf<Boolean>(false) }
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Grid(navController, editMode)
            Button(onClick = {
                editMode = !editMode
            }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(48.dp),
                contentPadding = PaddingValues(8.dp)) {
                if(editMode){
                    Image(
                        painter = painterResource(id = R.drawable.icon_check),
                        contentDescription = "Delete Button",
                        modifier = Modifier
                            .padding(0.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.icon_pencil),
                        contentDescription = "Delete Button",
                        modifier = Modifier
                            .padding(0.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }

    val streamDeckButtons = StreamDeckButtons()
    @Composable
    fun Grid(navController: NavController, editMode: Boolean) {
        //Grid
        var buttons = remember {streamDeckButtons.buttons}

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = gridItemWith.dp),
            contentPadding = PaddingValues(8.dp, 64.dp, 8.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(buttons) { index, button ->
                GridItem(index, navController, editMode, button, buttons)
            }
        }
    }

    @Composable
    fun GridItem(
        buttonIndex: Int,
        navController: NavController,
        editMode: Boolean,
        button: com.marklynch.steamdeck.data.buttons.StreamDeckButton,
        buttons: SnapshotStateList<com.marklynch.steamdeck.data.buttons.StreamDeckButton>
    ) {
        val angle by animateFloatAsState(targetValue = if (editMode) 10f else 0f)
        val context = LocalContext.current
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        // Track if the context menu is open
        var menuVisible by remember { mutableStateOf(false) }

        // Track the position of the click
        var clickOffset by remember { mutableStateOf(Offset.Zero) }
        val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            selectedImageUri = uri // Update the selected image URI
        }

        var refresh by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .graphicsLayer(rotationZ = angle)
        ) { // The size of the parent Box
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .size(Size.ORIGINAL) // Load original size
                        .build(),
                    placeholder = painterResource(button.iconImage),
                    error = painterResource(button.iconImage)
                ),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(color = colors[buttonIndex % colors.size]),
//                    .pointerInput(Unit) {
//                        detectTapGestures { offset ->
//                            if (editMode) {
//                                clickOffset = offset
//                                menuVisible = true
//                            } else {
//                                clickOffset = offset
//                            }
//                        }
//                    },
                contentScale = ContentScale.Crop
            )

            //DELETE ICON
            Image(
                painter = painterResource(id = R.drawable.icon_bin),
                contentDescription = "Delete Button",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.TopEnd)
                    .alpha(if (editMode) 1f else 0f)
                    .padding(8.dp)
                    .clickable {                        buttons.remove(button)
                    },
                contentScale = ContentScale.Crop)

            //IMAGE ICON
            Image(
                painter = painterResource(id = R.drawable.icon_image),
                contentDescription = "Edit Image",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd)
                    .alpha(if (editMode) 1f else 0f)
                    .padding(8.dp)
                    .clickable {
                        menuVisible = true
                    },
                contentScale = ContentScale.Crop)
        }

        DropdownMenu(
            expanded = menuVisible,
            onDismissRequest = { menuVisible = false },
            offset = DpOffset(clickOffset.x.dp, clickOffset.y.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Pick image from icons") },
                onClick = {
                    menuVisible = false
                })
            DropdownMenuItem(
                text = { Text("Pick image from phone") },
                onClick = {
                    launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    menuVisible = false
                })
        }
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
                modifier = Modifier
                    .size(80.dp)
                    .background(backgroundColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "IMAGE NAME")
                Text(text = "IMAGE DATE")
            }
        }
    }
}



