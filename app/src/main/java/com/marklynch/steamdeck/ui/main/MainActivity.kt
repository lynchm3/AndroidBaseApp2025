package com.marklynch.steamdeck.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.marklynch.steamdeck.BuildConfig
import com.marklynch.steamdeck.domain.printer.Printer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.*
import timber.log.Timber.Forest.plant
import javax.inject.Inject

val gridItemWith: Int = 128



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var printer: Printer
    @Inject lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(mainViewModel)
        }
    }
}