package no.kristiania.reverseimagesearch.viewmodel.api

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.jacksonandroidnetworking.JacksonParserFactory
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANResponse
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.Endpoints
import org.json.JSONArray
import java.io.File
import no.kristiania.reverseimagesearch.viewmodel.ResultViewModel

class FastNetworkingAPI(val context: Context) {

    init{
        AndroidNetworking.initialize(context)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun uploadImageSynchronous(bitmap: Bitmap): String? {

        val file = BitmapUtils.bitmapToFile(bitmap, "image.png", context)

        val request = AndroidNetworking.upload(Endpoints.upload_url)
            .addMultipartFile("image", file)
            .build()

        val response = request.executeForString()

        if(response.isSuccess){
            Log.i("POST_SUCCESS", "post successful!: " + response.result)
            return response.result.toString()
        } else {
            Log.i("POST_ERROR", "upload error: " + ANError().errorBody)
        }
        return null
    }

    enum class ImageProvider {
        Google, Bing, TinEye
    }

    fun getImageFromProviderSynchronous(url: String, provider: ImageProvider, viewModel: ResultViewModel): JSONArray? {

        val endpoint = when (provider) {
            ImageProvider.TinEye -> Endpoints.get_tinEye_url
            ImageProvider.Bing -> Endpoints.get_bing_url
            ImageProvider.Google -> Endpoints.get_google_url
        }

        val request = AndroidNetworking.get(endpoint)
            .addQueryParameter("url", url)
            .setTag("getTest")
            .setPriority(Priority.LOW)
            .build()

        val response = request.executeForJSONArray()

        if(response.isSuccess){
            Log.i("GET_SUCCESS", "get from $provider successful!")
            return response.result as JSONArray

        } else {
            Log.i("GET_ERROR", "get error from $provider: " + ANError().errorBody)
        }
        return null
    }
}