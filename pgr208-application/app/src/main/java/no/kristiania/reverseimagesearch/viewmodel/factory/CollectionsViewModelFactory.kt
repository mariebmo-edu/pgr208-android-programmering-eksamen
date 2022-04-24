package no.kristiania.reverseimagesearch.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.viewmodel.CollectionsViewModel

class CollectionsViewModelFactory(private val requestImageDao: RequestImageDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionsViewModel::class.java)) {
            return CollectionsViewModel(requestImageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}