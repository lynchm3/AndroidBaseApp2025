package com.marklynch.steamdeck.dependencyinjection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marklynch.steamdeck.AppDatabase
import com.marklynch.steamdeck.data.buttons.ButtonsDao
import com.marklynch.steamdeck.domain.printer.ConsolePrinter
import com.marklynch.steamdeck.domain.printer.Printer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrinterModule {
    @Binds
    @Singleton
    abstract fun bindPrinter(consolePrinter: ConsolePrinter): Printer
}

//@Module
//@InstallIn(MainActivity::class)
//abstract class MainViewModule {
//    @Binds
//    @Singleton
//    abstract fun bindMainViewModel(mainViewModel: MainViewModel): MainViewModel
//}

@Module
@InstallIn(SingletonComponent::class) // Makes it available throughout the application
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).setQueryCallback(RoomDatabase.QueryCallback { sqlQuery: String, bindArgs: List<Any?> ->
                Timber.d("SQL Query: $sqlQuery")
                // Optionally, log bound arguments if needed
                Timber.d("Arguments: $bindArgs")
        }, Executors.newSingleThreadExecutor()).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): ButtonsDao {
        return database.buttonsDao()
    }
}