package com.marklynch.steamdeck.data.buttons
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.marklynch.steamdeck.R

val colors: Array<Color> = arrayOf(
    Color(0xFF3D348B),// Dark Blue
    Color(0xFF7678ED),// Light Blue
    Color(0xFFF7B801), // Yellow
    Color(0xFFF18701),  // Light Orange
    Color(0xFFF35B04), // Dark Orange
)

val icons: Array<Int> = arrayOf(
    R.drawable.icon_chat,
    R.drawable.icon_check,
    R.drawable.icon_confetti,
    R.drawable.icon_game,
    R.drawable.icon_image,
    R.drawable.icon_pause,
    R.drawable.icon_pencil,
    R.drawable.icon_play,
    R.drawable.icon_plus,
    R.drawable.icon_stop
)

val texts: Array<String> = arrayOf(
    "Bin",
    "Chat",
    "Check",
    "Confetti",
    "Game",
    "Image"
)

enum class ButtonType {
    FIREWORKS
}

class StreamDeckButton {
    var iconImage = mutableIntStateOf(icons.random())
    var buttonType = mutableStateOf<ButtonType>(ButtonType.FIREWORKS)
    var selectedImageUri =  mutableStateOf<Uri?>(null)
    var color = mutableStateOf<Color>(colors.random())
    var text = mutableStateOf<String>(texts.random())
}