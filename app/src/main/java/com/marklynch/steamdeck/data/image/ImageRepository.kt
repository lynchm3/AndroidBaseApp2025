package com.marklynch.steamdeck.data.image

interface ImageRepository {
    suspend fun getAllImages(): List<ImageData>
}