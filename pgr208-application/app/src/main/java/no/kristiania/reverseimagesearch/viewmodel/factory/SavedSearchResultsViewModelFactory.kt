package no.kristiania.reverseimagesearch.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchResultsViewModel
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import java.lang.IllegalArgumentException

class SavedSearchResultsViewModelFactory(private val resultController: ResultController, private val requestImageId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedSearchResultsViewModel::class.java)) {
            return SavedSearchResultsViewModel(resultController, requestImageId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}