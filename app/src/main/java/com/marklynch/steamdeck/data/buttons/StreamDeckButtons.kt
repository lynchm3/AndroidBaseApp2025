package com.marklynch.steamdeck.data.buttons

import androidx.compose.runtime.mutableStateListOf

class StreamDeckButtons {
    var buttons = mutableStateListOf(StreamDeckButton(), StreamDeckButton())

//    fun removeStreamDeckButton(button:StreamDeckButton){
//        buttons = buttons.filter { it != button }
//    }
}