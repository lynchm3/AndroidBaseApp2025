package com.marklynch.steamdeck.ui.activity.main

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marklynch.steamdeck.data.buttons.ButtonType
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import com.marklynch.steamdeck.data.buttons.colors
import com.marklynch.steamdeck.data.buttons.icons
import com.marklynch.steamdeck.data.buttons.texts
import com.marklynch.steamdeck.ui.activity.pickinteraction.PickInteractionScreen

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, mainViewModel) }
        composable("add") { PickInteractionScreen(navController) }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel?
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
        Button(
            onClick = {
                mainViewModel?.insert(StreamDeckButton(0,
                    iconImage = mutableIntStateOf(icons.random()),
                    buttonType = mutableStateOf<ButtonType>(ButtonType.FIREWORKS),
                    selectedImageUri = mutableStateOf<Uri?>(null),
                    color = mutableStateOf<Color>(colors.random()),
                    text = mutableStateOf<String>(texts.random())
                ))
            }, modifier = Modifier.fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(text = "Grid")
        }
    }
}

@Preview
@Composable
fun SimpleComposablePreview() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, null) }
        composable("add") { PickInteractionScreen(navController) }
    }
}