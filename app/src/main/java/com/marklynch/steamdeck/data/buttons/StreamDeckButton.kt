package com.marklynch.steamdeck.data.buttons
import android.net.Uri
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity(tableName = "buttons")
data class StreamDeckButton (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var iconImage: MutableIntState,
    var buttonType: MutableState<ButtonType>,
    var selectedImageUri: MutableState<Uri?>,
    var color: MutableState<Color>,
    var text: MutableState<String>
)