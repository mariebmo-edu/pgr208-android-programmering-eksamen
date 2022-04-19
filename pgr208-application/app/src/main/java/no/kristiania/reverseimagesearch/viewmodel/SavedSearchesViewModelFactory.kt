package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import java.lang.IllegalArgumentException

class SavedSearchesViewModelFactory(private val requestImageDao: RequestImageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedSearchesViewModel::class.java)) {
            return SavedSearchesViewModel(requestImageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}