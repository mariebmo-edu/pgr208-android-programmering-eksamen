package no.kristiania.reverseimagesearch.viewmodel.api

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksonandroidnetworking.JacksonParserFactory
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANResponse
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.Endpoints
import org.json.JSONArray
import java.io.File


class FastNetworkingAPI(val context: Context) {

    init{
        AndroidNetworking.initialize(context)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun uploadImage(bitmap: Bitmap): String? {

        val file = BitmapUtils.bitmapToFile(bitmap, "image.png", context)

        val response = makeApiUploadCall(file)

        if(response.isSuccess){
            Log.i("POST_SUCCESS", "post successful!: " + response.result)
            return response.result.toString()
        } else {
            Log.i("POST_ERROR", "upload error: " + ANError().errorBody)
        }
        return null
    }

    fun makeApiUploadCall(file : File) : ANResponse<Any>{
        val req = AndroidNetworking.upload(Endpoints.upload_url)
            .addMultipartFile("image", file)
            .build()

        return req.executeForString()
    }

    enum class ImageProvider {
        Google, Bing, TinEye
    }

    fun getImageFromProvider(url: String, provider: ImageProvider): JSONArray? {

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