package no.kristiania.reverseimagesearch.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.viewmodel.SearchResultViewModel

class SearchResultViewModelFactory(private val requltController: ResultController) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(requltController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}