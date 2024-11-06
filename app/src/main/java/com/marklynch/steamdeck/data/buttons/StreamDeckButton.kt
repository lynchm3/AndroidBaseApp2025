package com.marklynch.steamdeck.data.buttons
import com.marklynch.steamdeck.R

enum class ButtonType {
    FIREWORKS
}

class StreamDeckButton {
    var iconImage:Int = R.drawable.icon_confetti
    var buttonType: ButtonType = ButtonType.FIREWORKS
}