package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class SavedSearchesViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {
    val savedSearchImages = requestImageDao.getAll()
    private val _navigateToResults = MutableLiveData<Long?>()
    val navigateToResults: LiveData<Long?>
        get() = _navigateToResults

    fun onRequestClicked(requestId: Long) {
        _navigateToResults.value = requestId
    }

    fun onNavigated() {
        _navigateToResults.value = null
    }

}