//package com.marklynch.steamdeck.ui.activity.grid
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.marklynch.steamdeck.BuildConfig
//import com.marklynch.steamdeck.domain.printer.Printer
//import dagger.hilt.android.AndroidEntryPoint
//import timber.log.Timber
//import timber.log.Timber.*
//import timber.log.Timber.Forest.plant
//import javax.inject.Inject
//
//val gridItemWith: Int = 128
//
//@AndroidEntryPoint
//class GridActivity : ComponentActivity() {
//
//    @Inject lateinit var printer: Printer
//    @Inject lateinit var gridViewModel: GridViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        if (BuildConfig.DEBUG) {
//            plant(DebugTree())
//        }
//        Timber.d("onCreate")
//        super.onCreate(savedInstanceState)
//        setContent {
//            MainScreen(gridViewModel)
//        }
//    }
//}