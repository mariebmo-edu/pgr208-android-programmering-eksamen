package no.kristiania.reverseimagesearch.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap


class ImageSearchFragment : Fragment() {
    //private lateinit var savedBtn: Button
    private lateinit var uploadBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var selectedImage: Bitmap
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            uri = requireArguments().getParcelable(IMG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_search, container, false)
        // savedBtn = view.findViewById(R.id.saved_btn)
        uploadBtn = view.findViewById(R.id.upload_btn)
        searchBtn = view.findViewById(R.id.search_btn)
        cropBtn = view.findViewById(R.id.crop_btn)
        imagePreview = view.findViewById(R.id.uploaded_image)
        uploadBtn.setOnClickListener {
            pickImageGallery()
        }
        if (uri != null) {
            selectedImage =
                BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
            imagePreview.setImageBitmap(selectedImage)
        }
//        supportFragmentManager.beginTransaction().apply{
//            replace(R.id.fragmentContainerView, NothingSelectedFragment())
//            addToBackStack(null)
//            commit()
//        }
        return view
    }

    companion object {

        private val IMG_URI = "imageUri"

        fun newInstance(uri: Uri): ImageSearchFragment {
            val fragment = ImageSearchFragment()
            val args = Bundle()
            args.putParcelable(IMG_URI, uri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBtn.setOnClickListener {
            uploadImageToServer(selectedImage)
        }
    }


    private fun uploadImageToServer(bitmap: Bitmap) {

        val http = FastNetworkingAPI()

        //val response = context?.let { http.uploadImage(bitmap, it) }
        //Log.d("UploadImageToServer", "response: $response")
        val navController = this.findNavController()

        fun getUrl(): String? {
            val response = http.uploadImageSynchronous(bitmap, context!!)
            Log.d("SearchFragment", "Runs blocking")

            if (response.isSuccess) {
                Log.d("Thread ${Thread.currentThread()}", "response is success")
                val url = response.result
                Log.d("url from server:", url.toString())
                return url.toString()
            } else {
                Log.e("Thread ${Thread.currentThread()}", response.error.toString())
                return null
            }
        }

        runBlocking(Dispatchers.IO) {
            val req = async { getUrl() }

            val res = req.await()
            res?.let {
                Log.d("Thread ${Thread.currentThread()}", "Executing navigation")
                view?.let {
                    val action = ImageSearchFragmentDirections
                        .actionSearchFragmentToResultFragment(it.toString())

                    withContext(Dispatchers.Main) {
                        navController
                            .navigate(action)
                    }

                }
            }

        }


    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                this.uri = it.data?.data!!
                selectedImage =
                    BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
                imagePreview.setImageBitmap(selectedImage)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE
//                supportFragmentManager.beginTransaction().apply{
//                    replace(R.id.fragmentContainerView, ImageSearchFragment.newInstance(it.data?.data!!))
//                    addToBackStack(null)
//                    commit()
//                }

            } else {
                println("error: result is not OK, or data is empty")
            }
        }
}