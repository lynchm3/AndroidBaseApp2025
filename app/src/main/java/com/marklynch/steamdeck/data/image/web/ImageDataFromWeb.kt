package com.marklynch.steamdeck.data.image.web

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException
import java.util.List
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


//const val API_URL = "https://api.github.com"

// Data model class for Contributor
data class Contributor(
    val login: String,
    val contributions: Int
)

// Service interface for GitHub API
interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    fun contributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<List<Contributor>>
}

// Main function to make the API call
//fun main() {
//    // Create Retrofit instance
//    val retrofit = Retrofit.Builder()
//        .baseUrl(API_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    // Create an instance of the GitHub API interface
//    val github = retrofit.create(GitHub::class.java)
//
//    // Create a call instance for retrieving contributors
//    val call = github.contributors("square", "retrofit")
//
//    // Fetch and print a list of contributors
//    try {
//        val contributors = call.execute().body()
//        contributors?.forEach { contributor ->
//            println("${contributor.login} (${contributor.contributions})")
//        }
//    } catch (e: IOException) {
//        println("Error: ${e.message}")
//    }
//}

//class ImageDataFromWebRepositoryImpl(private val context: Context) : ImageRepository {
//
//    override suspend fun getAllImages(): List<ImageData> {
//        return listOf()
//    }
//}
//

val EXAMPLE_IMAGE_ENDPOINT = "/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png"

interface ApiService {
    @GET
    fun downloadImage(@Url fileUrl: String): Call<ResponseBody>
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

fun downloadAndSaveImage(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = RetrofitInstance.apiService.downloadImage(EXAMPLE_IMAGE_ENDPOINT).execute()
        //saves to /data/user/0/com.marklynch.steamdeck/files/downloadedImage.png
        val savePath = File(context.filesDir, "downloadedImage.png")
        if (response.isSuccessful) {
            response.body()?.let { body ->
                saveFile(body, savePath)
            } ?: println("Failed to download image: Empty response body")
        } else {
            println("Error downloading image: ${response.code()}")
        }
    }
}

fun saveFile(body: ResponseBody, file: File) {
    try {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
            println("Image saved successfully at ${file.absolutePath}")
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: Exception) {
        println("Error saving file: ${e.message}")
    }
}