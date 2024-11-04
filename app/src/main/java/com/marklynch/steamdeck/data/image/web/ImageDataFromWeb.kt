package com.marklynch.steamdeck.data.image.web

import android.content.Context
import android.telecom.Call
import com.marklynch.steamdeck.data.image.ImageData
import com.marklynch.steamdeck.data.image.ImageRepository
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url


class ImageDataFromWebRepositoryImpl(private val context: Context) : ImageRepository {

    override suspend fun getAllImages(): List<ImageData> {
        return listOf()
    }
}

interface ApiService {
    @GET
    fun downloadImage(@Url fileUrl: String): Call
}

object RetrofitInstance {
    private const val BASE_URL = "https://www.google.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}