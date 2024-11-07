package com.marklynch.steamdeck.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
import com.marklynch.steamdeck.data.buttons.ButtonType
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import com.marklynch.steamdeck.data.buttons.StreamDeckButtons
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.data.image.resources.ImageDataFromResourcesRepositoryImpl
import timber.log.Timber
import timber.log.Timber.*
import timber.log.Timber.Forest.plant
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val gridItemWith: Int = 128

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

    val streamDeckButtons = StreamDeckButtons()

    @Composable
    fun HomeScreen(
        navController: NavController,
        imageRepository: ImageRepository
    ) {
        var editMode by remember { mutableStateOf<Boolean>(false) }
        var changeNameMode by remember { mutableStateOf<Boolean>(false) }
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
        val backgroundDrawable: Painter = painterResource(id = R.drawable.background)
        Box(
            modifier = Modifier
                .fillMaxSize(),
//            backgroundDrawable = backgroundDrawable


        ) {
            Image(painter = painterResource(id = R.drawable.background),
                contentDescription = "Delete Button",
                modifier = Modifier.padding(0.dp)
//                    .size(200.dp),
                    .fillMaxHeight(),
                contentScale = ContentScale.FillHeight
                )

            var triggerFireworks by remember { mutableStateOf(false) }
            Grid(navController, editMode, { triggerFireworks = true }, {changeNameMode = true})

            //Add button
            Button(
                onClick = {
                    streamDeckButtons.buttons.add(StreamDeckButton())
                }, modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
                    .size(48.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_plus),
                    contentDescription = "Delete Button",
                    modifier = Modifier.padding(0.dp),
                    contentScale = ContentScale.Fit
                )
            }

            //Edit button
            Button(
                onClick = {
                    editMode = !editMode
                }, modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(48.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                if (editMode) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_check),
                        contentDescription = "Delete Button",
                        modifier = Modifier.padding(0.dp),
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

            var text = remember { mutableStateOf("Hello, World!") }
            if(changeNameMode){
                TextField(
                    value = text.value,
                    onValueChange = { newText -> text.value = newText },
                    label = { Text("Enter text") }
                )
            }

            //Fireworks
            Fireworks(triggerFireworks, onAnimationEnd = { triggerFireworks = false })
        }
    }

    @Composable
    fun Grid(
        navController: NavController,
        editMode: Boolean,
        fireworksTrigger: () -> Unit,
        changeTextMode: () -> Unit
    ) {
        //Grid
        var buttons = remember { streamDeckButtons.buttons }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = gridItemWith.dp),
            contentPadding = PaddingValues(8.dp, 64.dp, 8.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(buttons.size) { index ->
                val button = buttons[index]
                GridItem(index, navController, editMode, button, buttons, fireworksTrigger, changeTextMode)
            }
        }
    }

    @Composable
    fun GridItem(
        buttonIndex: Int,
        navController: NavController,
        editMode: Boolean,
        button: StreamDeckButton,
        buttons: SnapshotStateList<StreamDeckButton>,
        fireworksTrigger: () -> Unit,
        changeTextMode: () -> Unit
    ) {
        val painter: Painter =
            if(button.selectedImageUri.value != null)
                rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(button.selectedImageUri.value)
                    .size(Size.ORIGINAL) // Load original size
                    .build())
            else
                painterResource(id = button.iconImage.intValue)



//            placeholder = painterResource(button.iconImage.intValue),
//            error = painterResource(button.iconImage.intValue)
//        )

        val angle by animateFloatAsState(targetValue = if (editMode) 10f else 0f)
        var menuVisible by remember { mutableStateOf(false) }

        // Track the position of the click
        var clickOffset by remember { mutableStateOf(Offset.Zero) }
        val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
//            selectedImageUri = uri // Update the selected image URI
            button.selectedImageUri.value = uri
        }

        //Streamdeck button
        Button(
            onClick = {
                if (button.buttonType.value == ButtonType.FIREWORKS) {
                    if (!editMode)
                        fireworksTrigger()
                }
            },
            modifier = Modifier
                .aspectRatio(1f)
                .graphicsLayer(rotationZ = angle),
            contentPadding = PaddingValues(0.dp),
            shape = RectangleShape
        ) {

            Box {

                Image(
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = button.color.value)
                        .padding(if (button.selectedImageUri.value == null) 24.dp else 0.dp),
                    contentScale = ContentScale.Crop
                )

                //text outline
                Text(
                    text = button.text.value,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                        .offset(x = 1.dp, y = 1.dp),
                    color = Color.Black,
                    maxLines = 1,
                    fontSize = TextUnit(4f, TextUnitType.Em),
                )

                //text
                Text(
                    text = button.text.value,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    color = Color.White,
                    maxLines = 1,
                    fontSize = TextUnit(4f, TextUnitType.Em),
                )

                if (editMode) {
                    //DELETE ICON
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .size(48.dp)
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clickable {
                                buttons.removeAt(buttonIndex)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_bin),
                            contentDescription = "Delete Button",
                            contentScale = ContentScale.Crop
                        )
                    }

                    //TEXT ICON
                    Box(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .size(48.dp)
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clickable {
                                changeTextMode()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_text),
                            contentDescription = "Change Text Button",
                            contentScale = ContentScale.Crop
                        )
                    }

                    //CHANGE IMAGE ICON
                    Box(
                        modifier = Modifier
                            .background(Color.Blue)
                            .size(48.dp)
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    if (editMode) {
                                        clickOffset = offset
                                        menuVisible = true
                                    } else {
                                        clickOffset = offset
                                    }
                                }
                            },
//                        .clickable { menuVisible = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_image),
                            contentDescription = "Edit Image",
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
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
    fun Fireworks(trigger: Boolean, onAnimationEnd: () -> Unit) {
        var particles by remember { mutableStateOf(emptyList<Particle>()) }

        // Trigger fireworks when "trigger" becomes true
        LaunchedEffect(trigger) {
            if (trigger) {
                particles = createFireworkParticles()

                val animationDuration = 2000
                val startTime = withFrameNanos { it }

                while (withFrameNanos { it } - startTime < animationDuration * 1_000_000) {
                    particles = particles.map { particle ->
                        particle.copy(
                            x = particle.x + particle.velocityX,
                            y = particle.y + particle.velocityY,
                            alpha = particle.alpha - 0.02f,
                            scale = particle.scale * 0.98f
                        )
                    }.filter { it.alpha > 0 }
                    withFrameNanos { }
                }

                onAnimationEnd() // Reset the trigger state after animation
            }
        }

        // Draw particles as circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                drawCircle(
                    color = particle.color.copy(alpha = particle.alpha),
                    radius = particle.scale * 5.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(particle.x, particle.y)
                )
            }
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
//                ImageItem(imageData, colors[index % colors.size])
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

// Data class for particle properties
data class Particle(
    var x: Float,
    var y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val scale: Float,
    var alpha: Float
)

// Function to create particles with random properties
fun createFireworkParticles(): List<Particle> {
    val particles = mutableListOf<Particle>()
    val centerX = 400f
    val centerY = 800f
    val numParticles = 100
    for (i in 0 until numParticles) {
        val angle = Random.nextDouble(0.0, Math.PI * 2).toFloat()
        val speed = Random.nextFloat() * 8f + 2f
        particles.add(
            Particle(
                x = centerX,
                y = centerY,
                velocityX = (cos(angle) * speed),
                velocityY = (sin(angle) * speed),
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                ),
                scale = Random.nextFloat() * 2f + 1f,
                alpha = 1f
            )
        )
    }
    return particles
}

@Composable
fun IconSelectionGrid() {

}



