package com.marklynch.steamdeck.ui.activity.grid

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marklynch.steamdeck.R
import com.marklynch.steamdeck.data.buttons.ButtonType
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import com.marklynch.steamdeck.data.buttons.colors
import com.marklynch.steamdeck.data.buttons.icons
import com.marklynch.steamdeck.data.buttons.texts
import com.marklynch.steamdeck.data.image.model.ImageViewModel
import com.marklynch.steamdeck.ui.Fireworks
import com.marklynch.steamdeck.ui.activity.pickinteraction.PickInteractionScreen
import com.marklynch.steamdeck.ui.activity.main.gridItemWith
import timber.log.Timber

@Composable
fun MainScreen(gridViewModel: GridViewModel) {
    val navController = rememberNavController()
    val imageViewModel = ImageViewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Grid(navController, gridViewModel) }
        composable("add") { PickInteractionScreen(navController) }
    }
}

@Composable
fun Grid(
    navController: NavController,
    gridViewModel: GridViewModel
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

        var triggerFireworks by remember { mutableStateOf(false) }
        Grid(navController, editMode, { triggerFireworks = true }, gridViewModel)

        //Add button
        Button(
            onClick = {
                gridViewModel.insert(StreamDeckButton(0,
                    iconImage = mutableIntStateOf(icons.random()),
                    buttonType = mutableStateOf<ButtonType>(ButtonType.FIREWORKS),
                    selectedImageUri = mutableStateOf<Uri?>(null),
                    color = mutableStateOf<Color>(colors.random()),
                    text = mutableStateOf<String>(texts.random())
                ))
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
    gridViewModel: GridViewModel
) {
    //Grid
    var buttons = gridViewModel.buttonsList

    Timber.d("Grid()")

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = gridItemWith.dp),
        contentPadding = PaddingValues(8.dp, 64.dp, 8.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons.size) { index ->
            val button = buttons[index]
            GridItem(
                index,
                navController,
                editMode,
                button,
                buttons,
                fireworksTrigger,
                gridViewModel
            )
        }
    }
}