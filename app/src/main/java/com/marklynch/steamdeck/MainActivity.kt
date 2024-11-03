package com.marklynch.steamdeck

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marklynch.steamdeck.ui.theme.SteamDeckTheme

var photos:Int = 20
var gridSize:Int = 128
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
        enableEdgeToEdge()
        setContent {
            SteamDeckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Grid(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Grid(modifier: Modifier) {
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SteamDeckTheme {
        Grid(Modifier)
    }
}

@Composable
fun PhotoItem(photo: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
//            .size(gridSize.dp)
//            .background(Color.Blue)
            .background(
                color = colors[photo%colors.size],
                shape = RoundedCornerShape(16.dp) )
    )
}



