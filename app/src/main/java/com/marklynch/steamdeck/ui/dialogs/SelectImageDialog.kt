package com.marklynch.steamdeck.ui.pickers

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SelectImageDialog(
    onDismissRequest: () -> Unit,
    onDrawableSelected: (Int) -> Unit,
    onImageSelected: (Uri?) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {

        var showDrawablesPicker by remember { mutableStateOf(false) }
        val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            onImageSelected(uri)
            onDismissRequest()
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp).background(Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp).background(Color.DarkGray)
            ) {
                Text(
                    text = "Select image from icons",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            showDrawablesPicker = true
                        },
                    color = Color.White
                )

                Text(
                    text = "Select image from device",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        },
                    color = Color.White
                )

                if (showDrawablesPicker) {
                    IconPickerDialog(
                        onDismissRequest = { showDrawablesPicker = false },
                        onDrawableSelected = { drawable ->
                            onDrawableSelected(drawable)
                            showDrawablesPicker = false
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}

