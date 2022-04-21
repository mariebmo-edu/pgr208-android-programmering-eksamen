package no.kristiania.reverseimagesearch.viewmodel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.JsonArrUtils
import java.io.File

class SearchViewModel : ViewModel() {
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    var shouldNavigate = true
    lateinit var tempImgFile : File

    private val _uri = MutableLiveData<Uri?>()
    val uri: LiveData<Uri?>
        get() = _uri


    private val _cropping = MutableLiveData<Boolean>()
    val cropping : LiveData<Boolean>
        get() = _cropping


    fun initiateCroppingValue() {
        _cropping.value = false
    }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

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

    fun cropImage(bitmap: Bitmap, cropImageView: CropImageView) {

        cropImageView.setImageBitmap(bitmap)
        _cropping.value = true
    }

    fun finishCropping(bitmap: Bitmap, imageView: ImageView) {

        imageView.setImageBitmap(bitmap)
        _cropping.value = false
    }

    fun pickImageGallery(galleryResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    fun pickImageCamera(context: Context, cameraResultLauncher: ActivityResultLauncher<Intent>, cameraPermissionRequest: ActivityResultLauncher<String>) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getBmpFromCamera(context, cameraResultLauncher)
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    fun getBmpFromCamera(context: Context, cameraResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileProvider = FileProvider.getUriForFile(
            context,
            "no.kristiania.reverseimagesearch.fileprovider",
            tempImgFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        cameraResultLauncher.launch(intent)
    }
}