package no.kristiania.reverseimagesearch.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.viewmodel.CollectionImagesViewModel

class CollectionImagesViewModelFactory(
    private val resultImageDao: ResultImageDao,
    private val requestImageId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionImagesViewModel::class.java)) {
            return CollectionImagesViewModel(resultImageDao, requestImageId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}