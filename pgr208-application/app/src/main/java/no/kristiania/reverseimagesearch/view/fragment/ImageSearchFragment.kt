package no.kristiania.reverseimagesearch.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.*
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.FragmentImageSearchBinding
import no.kristiania.reverseimagesearch.viewmodel.SearchViewModel
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap
import java.io.File


class ImageSearchFragment : Fragment() {

    private var _binding: FragmentImageSearchBinding? = null
    private val binding get() = _binding!!
    private var _viewModel: SearchViewModel? = null
    private val viewModel get() = _viewModel!!
    private lateinit var uploadBtn: Button
    private lateinit var cameraBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var cropImageView: CropImageView
    private lateinit var selectedImage: Bitmap
    private lateinit var tempImgFile: File
    private var uri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        _viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner
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
            viewModel.uploadImageAndFetchUrl(selectedImage, requireContext())
        }

        return view
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

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            getBmpFromCamera()
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun getBmpFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        tempImgFile = File.createTempFile(
            "tempImg",
            ".jpg",
            this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val fileProvider = FileProvider.getUriForFile(
            this.context!!,
            "no.kristiania.reverseimagesearch.fileprovider",
            tempImgFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        cameraResultLauncher.launch(intent)
    }

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            getBmpFromCamera()
        }
    }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                selectedImage = BitmapFactory.decodeFile(tempImgFile.absolutePath)
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