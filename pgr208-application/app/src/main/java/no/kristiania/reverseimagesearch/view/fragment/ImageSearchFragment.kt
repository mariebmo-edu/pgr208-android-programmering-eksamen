package no.kristiania.reverseimagesearch.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.lifecycle.ViewModelProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.FragmentImageSearchBinding
import no.kristiania.reverseimagesearch.viewmodel.SearchViewModel
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap


class ImageSearchFragment : Fragment() {

    private var _binding: FragmentImageSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var uploadBtn: Button
    private lateinit var cameraBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var cropImageView: CropImageView
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

        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        // binding.viewModel = viewModel ....hvorfor satte man denne igjen? om ting ikke funker, sjekk opp
        binding.lifecycleOwner = viewLifecycleOwner
        // savedBtn = view.findViewById(R.id.saved_btn)
        uploadBtn = binding.uploadBtn
        cameraBtn = binding.cameraBtn
        searchBtn = binding.searchBtn
        cropBtn = binding.cropBtn
        imagePreview = binding.uploadedImage
        cropImageView = binding.cropImageView

        uploadBtn.setOnClickListener {
            pickImageGallery()
        }

        cameraBtn.setOnClickListener {
            pickImageCamera()
        }

        if (uri != null) {
            selectedImage =
                BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
            imagePreview.setImageBitmap(selectedImage)
        }
        viewModel.url.observe(viewLifecycleOwner, { url ->
            val action = ImageSearchFragmentDirections
                .actionSearchFragmentToResultFragment(url)
            this.findNavController().navigate(action)
        })
        searchBtn.setOnClickListener {
            uploadImageToServer(selectedImage, viewModel)
        }

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
        // noen grunn til at click listener var her?
    }

    private fun cropImage(bitmap: Bitmap) {
        imagePreview.visibility = View.GONE
        searchBtn.visibility = View.GONE
        cropImageView.visibility = View.VISIBLE
        cropImageView.setImageBitmap(bitmap)
        cropBtn.text = getString(R.string.finish_cropping)
        cropBtn.setOnClickListener {
            finishCropping(cropImageView.croppedImage)
        }
    }

    private fun finishCropping(bitmap: Bitmap) {
        imagePreview.setImageBitmap(bitmap)
        cropImageView.visibility = View.GONE
        imagePreview.visibility = View.VISIBLE
        searchBtn.visibility = View.VISIBLE
        cropBtn.text = getString(R.string.crop_image)
        cropBtn.setOnClickListener {
            cropImage(selectedImage)
        }
    }

    private fun uploadImageToServer(bitmap: Bitmap, viewModel: SearchViewModel) {

        val http = FastNetworkingAPI()

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
        //val navController = this.findNavController()
        GlobalScope.launch(Dispatchers.IO) {
            val req = async { getUrl() }

            launch(Dispatchers.Main) {
                val res = req.await()
                Log.d("main", "Running on main")
                viewModel.setUrl(res.toString())
            }

        }


    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraResultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            cameraResultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                selectedImage = it.data?.extras?.get("data") as Bitmap
                imagePreview.setImageBitmap(selectedImage)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE
            }
        }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                this.uri = it.data?.data!!
                selectedImage =
                    BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
                imagePreview.setImageBitmap(selectedImage)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE

            } else {
                println("error: result is not OK, or data is empty")
            }
        }
}