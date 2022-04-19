package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import java.lang.IllegalArgumentException

class ResultViewModelFactory(private val requestImageDao: RequestImageDao,private val resultImageDao: ResultImageDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(requestImageDao, resultImageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel!")
    }
}