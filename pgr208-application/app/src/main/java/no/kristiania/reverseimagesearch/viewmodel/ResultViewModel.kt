package no.kristiania.reverseimagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.viewmodel.utils.DummyImagePaths
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import org.json.JSONArray

class ResultViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {


    private val _resultImages = MutableLiveData<List<ResultImage>>()
    val resultImages: LiveData<List<ResultImage>>
        get() = _resultImages

    fun fetchImagesFromSearch(response: JSONArray) {
        val imageObjs = mutableListOf<ResultImage>()

        for (i in 0 until response.length()) {
            val currentJsonObj = response.getJSONObject(i)
            val imageLink = currentJsonObj.getString("image_link")
            val thumbnailLink = currentJsonObj.getString("thumbnail_link")
            Log.d("fetchImagesFromSearch", currentJsonObj.toString())
            imageObjs.add(ResultImage(serverPath = imageLink))
        }

        _resultImages.value = imageObjs
    }
    //fun addItem(resultItem: ResultItem) = requestImageDao.add(resultItem)

    //fun removeItem(resultItem: ResultItem) = requestImageDao.remove(resultItem)

}