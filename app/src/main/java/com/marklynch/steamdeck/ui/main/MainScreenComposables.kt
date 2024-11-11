package com.marklynch.steamdeck.ui.main

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.PrimaryKey
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.marklynch.steamdeck.R
import com.marklynch.steamdeck.data.buttons.ButtonType
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import com.marklynch.steamdeck.data.buttons.colors
import com.marklynch.steamdeck.data.buttons.icons
import com.marklynch.steamdeck.data.buttons.texts
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.ui.Fireworks
import com.marklynch.steamdeck.ui.PickInteractionScreen
import com.marklynch.steamdeck.ui.dialogs.TextEntryDialog
import com.marklynch.steamdeck.ui.dialogs.SelectImageDialog
import timber.log.Timber

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val imageViewModel = ImageViewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, mainViewModel) }
        composable("add") { PickInteractionScreen(navController) }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {

    var editMode by remember { mutableStateOf<Boolean>(false) }

    if(editMode){
        BackHandler {
            editMode = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray)
    ) {
        //Background image
//        Image(painter = painterResource(id = R.drawable.background),
//            contentDescription = "Delete Button",
//            modifier = Modifier.padding(0.dp)
//                .fillMaxHeight(),
//            contentScale = ContentScale.FillHeight
//        )

        var triggerFireworks by remember { mutableStateOf(false) }
        Grid(navController, editMode, { triggerFireworks = true }, mainViewModel)

        //Add button
        Button(
            onClick = {
                mainViewModel.insert(StreamDeckButton(0,
                    iconImage = mutableIntStateOf(icons.random()),
                    buttonType = mutableStateOf<ButtonType>(ButtonType.FIREWORKS),
                    selectedImageUri = mutableStateOf<Uri?>(null),
                    color = mutableStateOf<Color>(colors.random()),
                    text = mutableStateOf<String>(texts.random())
                ))
//                streamDeckButtons.buttons.add(StreamDeckButton())
            }, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(8.dp)
                .size(48.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_plus),
                contentDescription = "Add Button",
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
                .size(48.dp)
                .semantics { contentDescription = "editMode" },
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(text = "Edit")

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
                    contentDescription = "Edit Button",
                    modifier = Modifier
                        .padding(0.dp),
                    contentScale = ContentScale.Fit
                )
            }
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
    mainViewModel: MainViewModel
) {
    //Grid
    var buttons = mainViewModel.buttonsList

    Timber.d("Grid()")

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = gridItemWith.dp),
        contentPadding = PaddingValues(8.dp, 64.dp, 8.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons.size) { index ->
            val button = buttons[index]
            GridItem(index, navController, editMode, button, buttons, fireworksTrigger, mainViewModel)
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
    mainViewModel: MainViewModel
) {

    Timber.d("GridItem()")
    val painter: Painter =
        if(button.selectedImageUri.value != null)
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(button.selectedImageUri.value)
                    .size(Size.ORIGINAL) // Load original size
                    .build())
        else
            painterResource(id = button.iconImage.intValue)

    val angle by animateFloatAsState(targetValue = if (editMode) 10f else 0f)

    // Track the position of the click
    var clickOffset by remember { mutableStateOf(Offset.Zero) }
    var showSelectImageDialog by remember { mutableStateOf(false) }
    var changeTextMode by remember { mutableStateOf<Boolean>(false) }
    var text by remember { button.text }

    //Streamdeck button
    Button(
        onClick = {
            if (button.buttonType.value == ButtonType.FIREWORKS) {
                if(!editMode)
                    fireworksTrigger()
//                printer.print()
            }
        },
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer(rotationZ = angle)
            .semantics { contentDescription = "StreamdeckButton$buttonIndex" },
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
                //Delete button
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .size(48.dp)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clickable {
                            mainViewModel.delete(button)
//                            buttons.removeAt(buttonIndex)
                            
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_bin),
                        contentDescription = "Delete Button",
                        contentScale = ContentScale.Crop
                    )
                }

                //Change text button
                Box(
                    modifier = Modifier
                        .background(Color.Yellow)
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable {
                            changeTextMode = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_text),
                        contentDescription = "Change Text Button",
                        contentScale = ContentScale.Crop
                    )
                }

                //Change image button
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
                                    showSelectImageDialog = true
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

    //Change name dialog
    if(changeTextMode){
        TextEntryDialog(
            onDismissRequest = {changeTextMode = false},
            startText = text,
            onValueChange = { newText -> text = newText })
    }

    //Select image dialog
    if(showSelectImageDialog)
        SelectImageDialog(
            onDismissRequest = { showSelectImageDialog = false },
            onDrawableSelected = { drawable ->
                button.iconImage.intValue = drawable
                button.selectedImageUri.value = null
            },
            onImageSelected = { uri ->
                button.selectedImageUri.value = uri
            }
        )

//    @Preview(showBackground = true)
//    @Composable
//    fun Preview() {
//        val navController = rememberNavController()
//        val context = LocalContext.current
//        val imageRepository = ImageDataFromResourcesRepositoryImpl(context)
//        NavHost(navController = navController, startDestination = "home") {
//            composable("home") {
//                HomeScreen(
//                    navController,
//                    mainViewModel
//                )
//            }
//            composable("add") { PickInteractionScreen(navController) }
//        }
//    }
}