package no.kristiania.reverseimagesearch.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.JsonArrUtils

class SearchViewModel : ViewModel() {
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url
    var shouldNavigate = true

    fun uploadImageForUrl(bitmap: Bitmap, context: Context) {
        val http = FastNetworkingAPI(context)

        GlobalScope.launch(Dispatchers.IO) {
            val res = async { http.uploadImageSynchronous(bitmap) }
            val url = res.await()

            url?.let {
                launch(Dispatchers.Main) {
                    Log.d("upload image from uri", "In main thread $it")
                    _url.value = it
                }
            }
        }
    }
}