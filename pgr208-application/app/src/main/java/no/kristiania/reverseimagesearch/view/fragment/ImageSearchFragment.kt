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
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.FragmentImageSearchBinding
import no.kristiania.reverseimagesearch.viewmodel.SearchViewModel
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

    // Denne må kanskje flyttes til SearchViewModel
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

        viewModel.tempImgFile = File.createTempFile(
            "tempImg",
            ".jpg",
            this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uploadBtn.setOnClickListener {
            pickImageGallery()
        }

        cameraBtn.setOnClickListener {
            pickImageCamera()
        }

        cropBtn.setOnClickListener {
            cropImage(imagePreview.drawable.toBitmap())
        }

        // Nødvendig for å unngå rot ved navigasjon
        if (uri != null) {
            val bitmap =
                BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
            imagePreview.setImageBitmap(bitmap)
            searchBtn.visibility = View.VISIBLE
            cropBtn.visibility = View.VISIBLE
        }

        viewModel.url.observe(viewLifecycleOwner, { url ->
            if (viewModel.shouldNavigate) {
                Log.d("URL OBSERVER", "Should navigate")
                val action = ImageSearchFragmentDirections
                    .actionSearchFragmentToResultFragment(url, uri.toString())
                this.findNavController().navigate(action)
                viewModel.shouldNavigate = false
            }
        })
        searchBtn.setOnClickListener {
            viewModel.shouldNavigate = true
            viewModel.uploadImageForUrl(imagePreview.drawable.toBitmap(), requireContext())
        }

        return view
    }

    // Burde flyttes ut til SearchViewModel
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

    // Burde flyttes ut til SearchViewModel
    private fun finishCropping(bitmap: Bitmap) {
        imagePreview.setImageBitmap(bitmap)
        viewModel.tempImgFile = BitmapUtils.bitmapToFile(bitmap, "tempImg.jpg", requireContext())
        uri = Uri.fromFile(viewModel.tempImgFile)
        cropImageView.visibility = View.GONE
        imagePreview.visibility = View.VISIBLE
        searchBtn.visibility = View.VISIBLE
        cropBtn.text = getString(R.string.crop_image)
        cropBtn.setOnClickListener {
            cropImage(imagePreview.drawable.toBitmap())
        }
    }

    // Burde flyttes ut til SearchViewModel
    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    // Burde flyttes ut til SearchViewModel
    private fun pickImageCamera() {
        if (ContextCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getBmpFromCamera()
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    // Burde flyttes ut til SearchViewModel
    private fun getBmpFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileProvider = FileProvider.getUriForFile(
            requireContext(),
            "no.kristiania.reverseimagesearch.fileprovider",
            viewModel.tempImgFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        cameraResultLauncher.launch(intent)
    }

    // Må ligge her; callback kan kanskje flyttes ut
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getBmpFromCamera()
            }
        }

    // Må ligge her; callback kan kanskje flyttes ut
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(viewModel.tempImgFile.absolutePath)
                uri = Uri.fromFile(viewModel.tempImgFile)
                imagePreview.setImageBitmap(bitmap)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE
            }
        }

    // Må ligge her; callback kan kanskje flyttes ut
    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                this.uri = it.data?.data!!
                val bitmap =
                    BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
                imagePreview.setImageBitmap(bitmap)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE

            } else {
                println("error: result is not OK, or data is empty")
            }

        }
}
