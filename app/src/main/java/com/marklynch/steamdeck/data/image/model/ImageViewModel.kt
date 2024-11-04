package com.marklynch.steamdeck.data.image.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    private val _images = MutableStateFlow<List<ImageData>>(emptyList())
    val images: StateFlow<List<ImageData>> = _images

    fun loadImages() {
        Timber.d("loadImages")
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = repository.getAllImages()
            Timber.d("imageList = $imageList")
            _images.value = imageList
        }
    }
}
