package no.kristiania.reverseimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url


    fun setUrl(url: String) {
        _url.value = url
    }

}