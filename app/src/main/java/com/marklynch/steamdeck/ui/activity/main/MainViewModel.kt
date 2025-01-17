package com.marklynch.steamdeck.ui.activity.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marklynch.steamdeck.data.buttons.ButtonsRepository
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val buttonsRepository: ButtonsRepository
) : ViewModel() {
    // MutableState to hold the list of buttons
    var buttonsList = mutableStateListOf<StreamDeckButton>()
        private set

    init {
        select()
    }

    // Function to load all buttons
    val lock = Any()
    private fun select() {
        viewModelScope.launch {
            var newList = buttonsRepository.getAll()
            buttonsList.clear()
            buttonsList.addAll(newList)
        }
    }

    // Function to insert a button
    fun insert(button: StreamDeckButton) {
        viewModelScope.launch {
            buttonsRepository.insert(button)
            select()  // Reload buttons after insertion
        }
    }

    // Function to delete a button
    fun delete(button: StreamDeckButton) {
        viewModelScope.launch {
            buttonsRepository.delete(button)
            select()  // Reload buttons after deletion
        }
    }
}