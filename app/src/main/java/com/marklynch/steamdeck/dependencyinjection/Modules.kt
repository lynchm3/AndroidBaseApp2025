package com.marklynch.steamdeck.dependencyinjection

import com.marklynch.steamdeck.domain.printer.ConsolePrinter
import com.marklynch.steamdeck.domain.printer.Printer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrinterModule {
    @Binds
    @Singleton
    abstract fun bindPrinter(consolePrinter: ConsolePrinter): Printer
}