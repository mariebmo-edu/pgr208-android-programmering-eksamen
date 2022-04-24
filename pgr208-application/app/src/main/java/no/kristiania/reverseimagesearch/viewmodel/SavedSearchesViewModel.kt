package no.kristiania.reverseimagesearch.viewmodel

import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.kristiania.reverseimagesearch.model.db.RequestImageDao
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class SavedSearchesViewModel(private val requestImageDao: RequestImageDao) : ViewModel() {
    lateinit var savedSearchImages: LiveData<List<RequestImage>>
    private val _navigateToResults = MutableLiveData<Long?>()
    val navigateToResults: LiveData<Long?>
        get() = _navigateToResults
    private var _infoMessage = MutableLiveData<String>("")
    val infoMessage get() = _infoMessage

    init {
        try {
            savedSearchImages = requestImageDao.getAll()
        } catch (e: SQLiteException) {
            _infoMessage.value = e.message.toString()
        }
    }
    fun onRequestClicked(requestId: Long) {
        _navigateToResults.value = requestId
    }

    fun onNavigated() {
        _navigateToResults.value = null
    }

}