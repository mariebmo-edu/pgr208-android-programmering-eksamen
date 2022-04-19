package no.kristiania.reverseimagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.JsonArrUtils
import org.json.JSONArray

class ResultViewModel(
    private val requestImageDao: RequestImageDao,
    private val resultImageDao: ResultImageDao
) : ViewModel() {


    private var _resultImages = MutableLiveData<MutableList<ResultImage>>()
    val resultImages: LiveData<MutableList<ResultImage>>
        get() = _resultImages

    init {
        _resultImages.value = ArrayList()
    }

    fun getResultFromUrl(url: String, api: FastNetworkingAPI) {

        viewModelScope.launch(Dispatchers.IO) {
//            val googleReq =
//                async {
//                    api.getImageFromProviderSynchronous(
//                        url,
//                        FastNetworkingAPI.ImageProvider.Google
//                    )
//                }
            val bingReq =
                async {
                    api.getImageFromProviderSynchronous(
                        url,
                        FastNetworkingAPI.ImageProvider.Bing
                    )
                }
//            val tinEyeReq =
//                async {
//                    api.getImageFromProviderSynchronous(
//                        url,
//                        FastNetworkingAPI.ImageProvider.TinEye
//                    )
//                }

            // val googleRes = googleReq.await()
            val bingRes = bingReq.await()
            //val tinEyeRes = tinEyeReq.await()

//            val mergedJson =
//                JsonArrUtils().multipleJsonArraysToOne(googleRes, bingRes, tinEyeRes)

            launch(Dispatchers.Main) {
                bingRes?.let {
                    fetchImagesFromSearch(it)
                }
            }
        }
    }

    fun saveResuestImage(requestImage: RequestImage) {
        viewModelScope.launch {
            requestImageDao.insert(requestImage)
        }
    }

    fun saveResultImage(resultImage: ResultImage) {
        viewModelScope.launch {
            resultImageDao.insert(resultImage)
        }
    }

    fun fetchImagesFromSearch(response: JSONArray) {
        val imageObjs = mutableListOf<ResultImage>()
        _resultImages.value?.let {
            imageObjs.addAll(it)

            for (i in 0 until response.length()) {
                val currentJsonObj = response.getJSONObject(i)
                val imageLink = currentJsonObj.getString("image_link")
                val thumbnailLink = currentJsonObj.getString("thumbnail_link")
                Log.d("fetchImagesFromSearch", currentJsonObj.toString())
                imageObjs?.add(ResultImage(serverPath = imageLink))
            }

            _resultImages.value = imageObjs
        }


    }
    //fun addItem(resultItem: ResultItem) = requestImageDao.add(resultItem)

    //fun removeItem(resultItem: ResultItem) = requestImageDao.remove(resultItem)

}