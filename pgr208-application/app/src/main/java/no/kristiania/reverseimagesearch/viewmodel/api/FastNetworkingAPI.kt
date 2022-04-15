package no.kristiania.reverseimagesearch.viewmodel.api

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.jacksonandroidnetworking.JacksonParserFactory

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANResponse
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.Endpoints
import org.json.JSONObject
import org.json.JSONArray

import com.androidnetworking.interfaces.JSONArrayRequestListener




class FastNetworkingAPI : Application(){

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(getApplicationContext())
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun uploadImage(bitmap: Bitmap, context: Context) {

        val file = BitmapUtils.bitmapToFile(bitmap, "image.png", context)

        AndroidNetworking.upload(Endpoints.upload_url)
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .setTag("uploadTest")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    println(response)
                    getImageFromProvider(response!!, ImageProvider.Google)
                    getImageFromProvider(response!!, ImageProvider.Bing)
                    getImageFromProvider(response!!, ImageProvider.TinyEye)

                }
                override fun onError(error: ANError?) {
                    println("upload error: " + error?.localizedMessage)
                }
            })
    }

    suspend fun uploadImageSynchronous(bitmap: Bitmap, context: Context): ANResponse<Any> {
        val file = BitmapUtils.bitmapToFile(bitmap, "image.png", context)

        val req = AndroidNetworking.upload(Endpoints.upload_url)
            .addMultipartFile("image", file)
            .build()
        Log.d("FN API", "Executed upload")
        return req.executeForString()
    }

    enum class ImageProvider{
        Google, Bing, TinyEye
    }

    fun getImageFromProvider(url: String, provider: ImageProvider) {

        val endpoint = when (provider) {
            ImageProvider.TinyEye -> Endpoints.get_tinyEye_url
            ImageProvider.Bing -> Endpoints.get_bing_url
            ImageProvider.Google -> Endpoints.get_google_url
        }

        AndroidNetworking.get(endpoint)
            .addQueryParameter("url", url)
            .setTag("getTest")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    println("success from ${provider}! response: ${response}")
                }

                override fun onError(error: ANError) {
                    println("Error from ${provider}: $error")
                }
            })

    }
}