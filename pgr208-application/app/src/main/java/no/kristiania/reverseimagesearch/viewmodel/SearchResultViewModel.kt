package no.kristiania.reverseimagesearch.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import org.json.JSONArray

class SearchResultViewModel(
    private val resultController: ResultController
) : ViewModel() {

    private var _shouldSearch = MutableLiveData(true)
    val shouldSearch: LiveData<Boolean> get() = _shouldSearch

    private var _shouldNavigateToSaved = MutableLiveData(false)
    val shouldNavigateToSaved: LiveData<Boolean> get() = _shouldNavigateToSaved
    private var _infoMessage = MutableLiveData<String>()
    val infoMessage get() = _infoMessage

    lateinit var requestImageLocalPath: String
    lateinit var hostedImageServerUrl: String
    private var _resultImages = MutableLiveData<MutableList<ResultImage>>(mutableListOf())

    val resultImages: LiveData<MutableList<ResultImage>>
        get() = _resultImages

    fun toggleNavigateToSaved() {
        _shouldNavigateToSaved.value?.let {
            _shouldNavigateToSaved.value = !it
        }
    }

    fun setInfoText(message: String) {
        _infoMessage.value = message
    }

    fun getResultFromUrl(url: String, api: FastNetworkingAPI) {
        // app. req. 3
        viewModelScope.launch(Dispatchers.IO) {
            val googleReq =
                async {
                    api.getImageFromProviderSynchronous(
                        url,
                        FastNetworkingAPI.ImageProvider.Google
                    )
                }
            val bingReq =
                async {
                    api.getImageFromProviderSynchronous(
                        url,
                        FastNetworkingAPI.ImageProvider.Bing
                    )
                }
            val tinEyeReq =
                async {
                    api.getImageFromProviderSynchronous(
                        url,
                        FastNetworkingAPI.ImageProvider.TinEye
                    )
                }

            withTimeout(20000L) {
                launch(Dispatchers.Main) {
                    bingReq.await()?.let {
                        fetchImagesFromSearch(it)
                    }
                    googleReq.await()?.let {
                        fetchImagesFromSearch(it)
                    }
                    tinEyeReq.await()?.let {
                        fetchImagesFromSearch(it)
                    }
                }
            }
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

    // Sub req. 8
    fun saveResult(context: Context, imagesToSave: List<ResultImage>, collectionName: String) {

        val bitmapRequestImage = BitmapUtils.getBitmap(
            context, null, requestImageLocalPath,
            BitmapUtils.Companion::UriToBitmap
        )
        val requestImage = RequestImage(
            serverPath = hostedImageServerUrl,
            data = BitmapUtils.bitmapToByteArray(bitmapRequestImage),
            collectionName = collectionName
        )

        // Async save. Sub. req. 7
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultController.saveAll(requestImage, imagesToSave)
            } catch (e: SQLiteException) {
                CoroutineScope(Dispatchers.Main).launch {
                    setInfoText(e.message.toString())
                }
            }
        }

    }

    fun searchDone() {
        _shouldSearch.value = false
    }
}