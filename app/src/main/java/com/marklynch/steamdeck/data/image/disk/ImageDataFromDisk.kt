package com.marklynch.steamdeck.data.image.disk

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

data class ImageData(val uri: Uri, val name: String, val dateTaken: Long)

interface ImageRepository {
    suspend fun getAllImages(): List<ImageData>
}

class ImageRepositoryImpl(private val context: Context) : ImageRepository {

    override suspend fun getAllImages(): List<ImageData> {
        val images = mutableListOf<ImageData>()
        val contentResolver: ContentResolver = context.contentResolver

        // Define URI and projection for querying images
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )

        // Query the content resolver for images
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val dateTaken = cursor.getLong(dateTakenColumn)
                val contentUri = Uri.withAppendedPath(uri, id.toString())

                images.add(ImageData(uri = contentUri, name = name, dateTaken = dateTaken))
            }
        }

        return images
    }
}
