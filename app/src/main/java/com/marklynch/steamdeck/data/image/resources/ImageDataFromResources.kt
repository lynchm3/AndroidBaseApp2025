package com.marklynch.steamdeck.data.image.resources

import android.content.Context
import android.net.Uri
import com.marklynch.steamdeck.R
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository

val iconList = arrayOf(R.drawable.icon_chat,
    R.drawable.icon_confetti,
    R.drawable.icon_game,
    R.drawable.icon_pause,
    R.drawable.icon_stop,
    R.drawable.icon_play)

class ImageDataFromResourcesRepositoryImpl(private val context: Context) : ImageRepository {

    override suspend fun getAllImages(): List<ImageData> {
        val images = mutableListOf<ImageData>()
        for(icon in iconList)
            images.add(getDrawableUri(context, icon))
        return images
    }

    private fun getDrawableUri(context: Context, drawableResId: Int): ImageData {
        return ImageData(Uri.parse("android.resource://${context.packageName}/$drawableResId"))
    }
}