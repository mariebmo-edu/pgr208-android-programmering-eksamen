package no.kristiania.reverseimagesearch.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI

class SearchViewModel : ViewModel() {
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    fun uploadImageAndFetchUrl(bitmap: Bitmap, context: Context): Unit {
        val http = FastNetworkingAPI()


        GlobalScope.launch(Dispatchers.IO) {
            val response = http.uploadImageSynchronous(bitmap, context!!)
            Log.d("SearchFragment", "Runs on IO")

            if (response.isSuccess) {
                Log.d("Thread ${Thread.currentThread()}", "response is success")
                val url = response.result

                // Publishes result to UI
                launch(Dispatchers.Main) {
                    Log.d("main", "Running on main")
                    _url.value = response.result.toString()
                }
                Log.d("url from server:", url.toString())
            } else {
                // TODO: Error handling
                Log.e("Thread ${Thread.currentThread()}", response.error.toString())
            }

        }
    }
}