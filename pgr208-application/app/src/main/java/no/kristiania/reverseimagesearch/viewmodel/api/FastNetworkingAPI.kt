package no.kristiania.reverseimagesearch.viewmodel.api

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.jacksonandroidnetworking.JacksonParserFactory
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.ANResponse
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.Endpoints
import org.json.JSONArray
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.gson.JsonArray
import kotlinx.coroutines.*


class FastNetworkingAPI : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(getApplicationContext())
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun uploadImage(bitmap: Bitmap, context: Context): JsonArray {

        val file = BitmapUtils.bitmapToFile(bitmap, "image.png", context)

        val jsonArray = JsonArray()

        AndroidNetworking.upload(Endpoints.upload_url)
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .setTag("uploadTest")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    println(response)
                    getImagesFromAllProviders(response)
                    println(jsonArray)
                }
                override fun onError(error: ANError?) {
                    println("upload error: " + error?.localizedMessage)
                }
            })

        return jsonArray
    }

    enum class ImageProvider {
        Google, Bing, TinyEye
    }

    fun getImageFromProvider(url: String, provider: ImageProvider): JSONArray {

        val endpoint = when (provider) {
            ImageProvider.TinyEye -> Endpoints.get_tinyEye_url
            ImageProvider.Bing -> Endpoints.get_bing_url
            ImageProvider.Google -> Endpoints.get_google_url
        }

        var jsonResponse = JSONArray()

        AndroidNetworking.get(endpoint)
            .addQueryParameter("url", url)
            .setTag("getTest")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    println("success from ${provider}! response: $response")

                    jsonResponse = response
                }

                override fun onError(error: ANError) {
                    println("Error from ${provider}: ${error.errorBody}")
                }
            })

        return jsonResponse
    }

    fun getImagesFromAllProviders(url: String): JSONArray {
        val jsonResponse = JSONArray()

        fun addToResponse(arr: JSONArray) {
            println("in add to response: " + arr.length())
            for (i in 0 until arr.length()) {
                jsonResponse.put(arr[i])
            }
        }

        runBlocking {
            val requests = listOf(
                async(Dispatchers.IO) { addToResponse(getImageFromProvider(url, ImageProvider.Google))},// get first GET synchronous in own coroutine
                async(Dispatchers.IO) { addToResponse(getImageFromProvider(url, ImageProvider.Bing))}, // get second GET synchronous in own coroutine
                async(Dispatchers.IO) { addToResponse(getImageFromProvider(url, ImageProvider.TinyEye))} // get third GET synchronous in own coroutine
            )
            requests.awaitAll()
        }

        println("AllProviders: $jsonResponse")
        return jsonResponse
    }
}