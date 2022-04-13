package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.DummyImagePaths
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class ResultViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {

    private val _resultImages = MutableLiveData<List<ResultImage>>()
    val resultImages: LiveData<List<ResultImage>>
        get() = _resultImages

    init {
        val paths = mutableListOf<ResultImage>()
        DummyImagePaths.IMAGES.values.forEachIndexed { i, v ->
            paths.add(ResultImage(id = i * 1L, serverPath = v))
        }
        _resultImages.value = paths
    }
    //fun addItem(resultItem: ResultItem) = requestImageDao.add(resultItem)

    //fun removeItem(resultItem: ResultItem) = requestImageDao.remove(resultItem)

}