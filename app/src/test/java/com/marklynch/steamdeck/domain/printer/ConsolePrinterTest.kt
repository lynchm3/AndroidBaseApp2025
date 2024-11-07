package com.marklynch.steamdeck.domain.printer

import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsolePrinterTest {

    private lateinit var consolePrinter: ConsolePrinter
    private val outputStreamCaptor = ByteArrayOutputStream()

    @Before
    fun setUp() {
        consolePrinter = ConsolePrinter()
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @After
    fun tearDown() {
        System.setOut(System.out)
    }

    @Test
    fun `print should output correct message to console`() {
        consolePrinter.print()
        assertEquals("Printing from ConsolePrinter class!", outputStreamCaptor.toString().trim())
    }

}