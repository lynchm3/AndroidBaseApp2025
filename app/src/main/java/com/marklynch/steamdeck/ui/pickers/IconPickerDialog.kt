package com.marklynch.steamdeck.ui.pickers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.marklynch.steamdeck.R

val icons = listOf(
    R.drawable.icon_chat,
    R.drawable.icon_check,
    R.drawable.icon_confetti,
    R.drawable.icon_game,
    R.drawable.icon_image,
    R.drawable.icon_pause,
    R.drawable.icon_pencil,
    R.drawable.icon_play,
    R.drawable.icon_plus,
    R.drawable.icon_stop,
    R.drawable.icon_text
)

@Composable
fun IconPickerDialog(
    onDismissRequest: () -> Unit,
    onDrawableSelected: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp).background(Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp).background(Color.DarkGray)
            ) {
                Text(
                    text = "Select Drawable",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 columns for a grid layout
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth().background(Color.DarkGray)
                ) {
                    items(icons.size) { index ->
                        Image(
                            painter = painterResource(id = icons[index]),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(64.dp)
                                .clickable { onDrawableSelected(icons[index]) }.background(Color.DarkGray)
                        )
                    }
                }
            }
        }
    }
}

