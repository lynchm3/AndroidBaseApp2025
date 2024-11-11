package com.marklynch.steamdeck.data.buttons

import timber.log.Timber
import javax.inject.Inject

class ButtonsRepository @Inject constructor(
    private val buttonsDao: ButtonsDao
) {
    suspend fun getAll(): List<StreamDeckButton> {
        val buttons = buttonsDao.getAll()
        Timber.d("Buttons: $buttons")
        return buttons

//        return buttonsDao.getAll()
    }

    suspend fun insert(button: StreamDeckButton) {
        Timber.d("Inserting button: $button")
        buttonsDao.insert(button)
    }

    suspend fun delete(button: StreamDeckButton) {
        buttonsDao.delete(button)
    }
}