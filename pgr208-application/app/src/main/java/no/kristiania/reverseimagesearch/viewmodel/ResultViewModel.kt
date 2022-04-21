package no.kristiania.reverseimagesearch.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.JsonArrUtils
import org.json.JSONArray

class ResultViewModel(
    private val requestImageDao: RequestImageDao,
    private val resultImageDao: ResultImageDao,
) : ViewModel() {


    lateinit var requestImageLocalPath: String
    lateinit var hostedImageServerUrl: String
    private var _resultImages = MutableLiveData<MutableList<ResultImage>>()
    val resultImages: LiveData<MutableList<ResultImage>>
        get() = _resultImages

    init {
        _resultImages.value = ArrayList()
    }

    fun getResultFromUrl(url: String, api: FastNetworkingAPI) {

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


    fun saveAllResultImages(resultImages: List<ResultImage>) {
        viewModelScope.launch {
            resultImageDao.insertMany(resultImages)
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

    fun saveResult(context: Context, imagesToSave: List<ResultImage>, collectionName : String) {

        val bitmapRequestImage = BitmapUtils.getBitmap(context, null, requestImageLocalPath,
            BitmapUtils.Companion::UriToBitmap
        )
        val requestImage = RequestImage(serverPath = hostedImageServerUrl, data = BitmapUtils.bitmapToByteArray(bitmapRequestImage), collectionName = collectionName)

        viewModelScope.launch {
            val reqSave = async {requestImageDao.insert(requestImage)}
            val reqImgId = reqSave.await()

            imagesToSave.forEach {
                it.requestImageId = reqImgId
            }
            resultImageDao.insertMany(imagesToSave)
        }

    }
}