package com.marklynch.steamdeck.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marklynch.steamdeck.data.buttons.ButtonsRepository
import com.marklynch.steamdeck.data.buttons.StreamDeckButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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
    private fun select() {
        viewModelScope.launch {
            buttonsList.clear()
            buttonsList.addAll(buttonsRepository.getAll())
            Timber.d("Buttons loaded: ${buttonsList.size}")
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