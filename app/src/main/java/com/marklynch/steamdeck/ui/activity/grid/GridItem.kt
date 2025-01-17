package com.marklynch.steamdeck.ui.activity.grid

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.marklynch.steamdeck.R
import com.marklynch.steamdeck.data.buttons.ButtonType
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import com.marklynch.steamdeck.ui.dialogs.SelectImageDialog
import com.marklynch.steamdeck.ui.dialogs.TextEntryDialog
import timber.log.Timber


@Composable
fun GridItem(
    buttonIndex: Int,
    navController: NavController,
    editMode: Boolean,
    button: StreamDeckButton,
    buttons: SnapshotStateList<StreamDeckButton>,
    fireworksTrigger: () -> Unit,
    gridViewModel: GridViewModel
) {

    Timber.d("GridItem() - button.selectedImageUri.value = %s", button.selectedImageUri.value)
    if(button.selectedImageUri.toString() == "null"){
        Timber.d("GridItem() - button.selectedImageUri.value is the string null")
    } else if(button.selectedImageUri.value == null){
        Timber.d("GridItem() - button.selectedImageUri.value is actually null")
    }

    Timber.d("GridItem() - button.iconImage.intValue = %s", button.iconImage.intValue)
    Timber.d("GridItem() - R.drawable.icon_check= %s", R.drawable.icon_check)

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
                            gridViewModel.delete(button)
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
            onDismissRequest = {
                changeTextMode = false
                gridViewModel.insert(button)
            },
            startText = text,
            onValueChange = {
                    newText -> text = newText
            })
    }

    //Select image dialog
    if(showSelectImageDialog)
        SelectImageDialog(
            onDismissRequest = { showSelectImageDialog = false },
            onDrawableSelected = { drawable ->
                button.iconImage.intValue = drawable
                button.selectedImageUri.value = null
                gridViewModel.insert(button)
            },
            onImageSelected = { uri ->
                button.selectedImageUri.value = uri
                gridViewModel.insert(button)
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