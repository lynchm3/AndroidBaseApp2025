package com.marklynch.steamdeck.data

import android.net.Uri
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.marklynch.steamdeck.data.buttons.ButtonType

class MutableStateConverter {

    @TypeConverter
    fun fromMutableState(value: MutableState<String>): String {
        return value.value
    }

    @TypeConverter
    fun toMutableState(value: String): MutableState<String> {
        return mutableStateOf(value)
    }

    @TypeConverter
    fun fromMutableStateColor(value: MutableState<Color>): Int {
        return value.value.toArgb()
    }

    @TypeConverter
    fun toMutableStateColor(value: Int): MutableState<Color> {
        return mutableStateOf(Color(value))
    }

    @TypeConverter
    fun fromMutableStateUri(value: MutableState<Uri?>): String {
        if (value.value == null)
            return ""
        return value.value.toString()
    }

    @TypeConverter
    fun toMutableStateUri(value: String): MutableState<Uri?> {
        if(value == "")
            return mutableStateOf(null)
        return mutableStateOf(Uri.parse(value))
    }

    @TypeConverter
    fun fromMutableStateButtonType(value: MutableState<ButtonType>): String {
        return value.value.name
    }

    @TypeConverter
    fun toMutableStateButtonType(value: String): MutableState<ButtonType> {
        return mutableStateOf(ButtonType.valueOf(value))
    }

    @TypeConverter
    fun fromMutableIntState(value: MutableIntState): Int {
        return value.intValue
    }

    @TypeConverter
    fun toMutableIntState(value: Int): MutableIntState {
        return mutableIntStateOf(value)
    }
}