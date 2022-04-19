package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao

class SavedSearchesViewModel(requestImageDao: RequestImageDao) : ViewModel() {
    val requestImages = requestImageDao.getAll()
}