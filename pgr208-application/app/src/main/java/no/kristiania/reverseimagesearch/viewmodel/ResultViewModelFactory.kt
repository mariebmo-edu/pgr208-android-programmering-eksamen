package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import java.lang.IllegalArgumentException

class ResultViewModelFactory(private val dao: RequestImageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}