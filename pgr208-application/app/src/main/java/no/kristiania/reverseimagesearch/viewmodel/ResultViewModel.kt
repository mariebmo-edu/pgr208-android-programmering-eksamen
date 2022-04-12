package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao

class ResultViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {

    val resultItems = requestImageDao.getAll()

    //fun addItem(resultItem: ResultItem) = requestImageDao.add(resultItem)

    //fun removeItem(resultItem: ResultItem) = requestImageDao.remove(resultItem)

}