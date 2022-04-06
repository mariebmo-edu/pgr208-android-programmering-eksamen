package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.entity.ResultItem

class ResultViewModel : ViewModel() {
    private val resultItems = mutableListOf(ResultItem("url1"), ResultItem("url2"))
    val images: LiveData<List<ResultItem>> = MutableLiveData(resultItems)

    fun addItem(resultItem: ResultItem) = resultItems.add(resultItem)

    fun removeItem(resultItem: ResultItem) = resultItems.remove(resultItem)

}