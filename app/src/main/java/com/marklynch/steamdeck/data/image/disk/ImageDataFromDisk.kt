package com.marklynch.steamdeck.data.image.disk

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import timber.log.Timber

class ImageDataFromDiskRepositoryImpl(private val context: Context) : ImageRepository {

    override suspend fun getAllImages(): List<ImageData> {

        Timber.d("getAllImages")

        val images = mutableListOf<ImageData>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
        null, null, "$orderBy DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(uri, id.toString())
                images.add(ImageData(uri = contentUri))
            }
        }
        return images
    }
}
