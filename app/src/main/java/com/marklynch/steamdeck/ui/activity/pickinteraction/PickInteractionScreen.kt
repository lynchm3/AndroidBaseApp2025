package com.marklynch.steamdeck.ui.activity.pickinteraction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.marklynch.steamdeck.R
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.marklynch.steamdeck.ui.activity.main.gridItemWith


@Composable
fun PickInteractionScreen(navController: NavController) {
    //Grid

    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = gridItemWith.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("GENERAL")
        }
        item {
            Spacer(modifier = Modifier.size(1.dp))
        }
        item {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.icon_plus),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        color = Color.Blue,
                        shape = RoundedCornerShape(16.dp)
                    ).clickable {
                        navController.navigate("add")
                    }
            )
        }
        item {
            Spacer(modifier = Modifier.size(1.dp))
        }
        item {
            Text("OBS")
        }
        item {
            Spacer(modifier = Modifier.size(1.dp))
        }
        item {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.icon_plus),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        color = Color.Blue,
                        shape = RoundedCornerShape(16.dp)
                    ).clickable {
                        navController.navigate("add")
                    }
            )
        }
        item {
            Spacer(modifier = Modifier.size(1.dp))
        }
        item {
            LoadImageFromDisk(context.filesDir.toString() + "downloadedImage.png")
        }

    }
}

@Composable
fun LoadImageFromDisk(filePath: String) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(filePath) {
        val bitmap = BitmapFactory.decodeFile(filePath)
        imageBitmap = bitmap?.asImageBitmap()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Loaded image from disk",
                modifier = Modifier.fillMaxSize()
            )
        } ?: Text("Loading...")
    }
}