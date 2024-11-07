package com.marklynch.steamdeck.domain.printer

import timber.log.Timber
import javax.inject.Inject

class ConsolePrinter @Inject constructor() : Printer {
    override fun print() {
        Timber.d("Printing from ConsolePrinter class!")
    }
}