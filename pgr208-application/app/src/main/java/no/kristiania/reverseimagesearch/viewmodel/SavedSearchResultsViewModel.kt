package no.kristiania.reverseimagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class SavedSearchResultsViewModel(
    private val resultImageDao: ResultImageDao,
    private val requestImgId: Long
) : ViewModel() {
    val resultImages = resultImageDao.getByParentId(requestImgId)
}