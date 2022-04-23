package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class SavedSearchResultsViewModel(private val resultImageDao: ResultImageDao, private val requestImgId: Long) : ViewModel() {
    val resultImages = this.resultImageDao.getByParentId(requestImgId)
   // val resultImages: LiveData<List<ResultImage>> = MutableLiveData(listOf(ResultImage(serverPath = "test")))
}