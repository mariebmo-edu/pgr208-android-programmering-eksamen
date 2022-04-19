package no.kristiania.reverseimagesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class SavedSearchesViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {
    private val _savedSearchImages = MutableLiveData<List<RequestImage>>()

    val savedSearchImages: LiveData<List<RequestImage>> = requestImageDao.getAll()

    init {
        _savedSearchImages.value = listOf(RequestImage(serverPath = "Test"))
    }
}