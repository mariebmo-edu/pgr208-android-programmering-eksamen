package no.kristiania.reverseimagesearch.viewmodel.api

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.jacksonandroidnetworking.JacksonParserFactory

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.Endpoints

class Http : Application(){

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
                }
                override fun onError(error: ANError?) {
                    println("upload error: " + error?.localizedMessage)
                }
            })
    }
}