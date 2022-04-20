package no.kristiania.reverseimagesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class SavedSearchesViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {
    val savedSearchImages = requestImageDao.getAll()
}