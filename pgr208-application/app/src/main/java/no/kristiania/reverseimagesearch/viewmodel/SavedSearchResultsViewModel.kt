package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class SavedSearchResultsViewModel(
    private val resultController: ResultController,
    private val requestImgId: Long
) : ViewModel() {
    //val resultImages = resultImageDao.getByParentId(requestImgId)
    lateinit var resultImages: LiveData<List<ResultImage>>

    init {
        fetchImages()
    }

    fun fetchImages() {
        viewModelScope.launch {

            val req = async { resultController.getByParentId(requestImgId) }

            launch(Dispatchers.Main) {
                resultImages = req.await()
            }
        }
    }
    // val resultImages: LiveData<List<ResultImage>> = MutableLiveData(listOf(ResultImage(serverPath = "test")))
}