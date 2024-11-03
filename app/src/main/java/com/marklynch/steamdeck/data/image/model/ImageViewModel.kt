package com.marklynch.steamdeck.data.image.model

import com.marklynch.steamdeck.data.image.disk.ImageData
import com.marklynch.steamdeck.data.image.disk.ImageRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    private val _images = MutableStateFlow<List<ImageData>>(emptyList())
    val images: StateFlow<List<ImageData>> = _images

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = repository.getAllImages()
            _images.value = imageList
        }
    }
}
