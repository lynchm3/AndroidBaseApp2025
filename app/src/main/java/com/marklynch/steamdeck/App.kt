package com.marklynch.steamdeck

import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marklynch.steamdeck.data.MutableStateConverter
import com.marklynch.steamdeck.data.buttons.ButtonsDao
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import dagger.hilt.android.HiltAndroidApp

@TypeConverters(MutableStateConverter::class)
@Database(entities = [StreamDeckButton::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buttonsDao(): ButtonsDao
}

@HiltAndroidApp
class App : Application() {

}