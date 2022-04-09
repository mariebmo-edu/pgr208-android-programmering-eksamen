package no.kristiania.reverseimagesearch.viewmodel.api

import android.app.Application
import android.net.Uri
import com.jacksonandroidnetworking.JacksonParserFactory

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError

import org.json.JSONObject

import com.androidnetworking.interfaces.JSONObjectRequestListener
import java.io.File


class Http : Application(){

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    fun uploadImage(image : File){
        AndroidNetworking.upload("http://api-edu.gtl.ai/ api/v1/imagesearch/upload")
            .addMultipartFile("image", image)
            .addMultipartParameter("key", "value")
            .setTag("uploadTest")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    println(response)
                }

                override fun onError(error: ANError) {
                    // handle error
                    println("upload error!!")
                }
            })

    }
}