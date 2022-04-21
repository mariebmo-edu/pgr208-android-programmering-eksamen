package no.kristiania.reverseimagesearch.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        _viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        viewModel.initiate()

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

        //println(Uri.fromFile(viewModel.tempImgFile).toString())

        uploadBtn.setOnClickListener {
            viewModel.pickImageGallery(galleryResultLauncher)
        }

        cameraBtn.setOnClickListener {
            viewModel.pickImageCamera(requireContext(), cameraResultLauncher, cameraPermissionRequest)
        }

        cropBtn.setOnClickListener {
            viewModel.cropImage(imagePreview.drawable.toBitmap(), cropImageView)
        }

        // Nødvendig for å unngå rot ved navigasjon
        if (viewModel.uri != null) {
            val bitmap =
                BitmapUtils.getBitmap(requireContext(), null, viewModel.uri.toString(), ::UriToBitmap)
            imagePreview.setImageBitmap(bitmap)
            searchBtn.visibility = View.VISIBLE
            cropBtn.visibility = View.VISIBLE
        } else {
            searchBtn.visibility = View.GONE
            cropBtn.visibility = View.GONE
        }

        viewModel.url.observe(viewLifecycleOwner, { url ->
            if (viewModel.shouldNavigate) {
                Log.d("URL OBSERVER", "Should navigate")
                val action = ImageSearchFragmentDirections
                    .actionSearchFragmentToResultFragment(url, viewModel.uri.toString())
                this.findNavController().navigate(action)
                viewModel.shouldNavigate = false
            }
        })

        viewModel.cropping.observe(viewLifecycleOwner, {
            if (it) {
                cropOn()
            } else if (viewModel.uri != null && !it) {
                cropOff()
            }
        })

        searchBtn.setOnClickListener {
            viewModel.shouldNavigate = true
            viewModel.uploadImageForUrl(imagePreview.drawable.toBitmap(), requireContext())
        }

        return view
    }

    private fun cropOn() {
        imagePreview.visibility = View.GONE
        searchBtn.visibility = View.GONE
        uploadBtn.visibility = View.GONE
        cameraBtn.visibility = View.GONE
        cropImageView.visibility = View.VISIBLE
        cropBtn.text = getString(R.string.finish_cropping)
        cropBtn.setOnClickListener {
            viewModel.finishCropping(cropImageView.croppedImage, imagePreview)
        }
    }

    private fun cropOff() {

        fun helper() {
            cropImageView.visibility = View.GONE
            imagePreview.visibility = View.VISIBLE
            searchBtn.visibility = View.VISIBLE
            uploadBtn.visibility = View.VISIBLE
            cameraBtn.visibility = View.VISIBLE
            cropBtn.text = getString(R.string.crop_image)
        }

        val bitmap: Bitmap? = cropImageView.croppedImage

        bitmap?.let { bmp ->
            viewModel.tempImgFile =
                BitmapUtils.bitmapToFile(bmp, "tempImg.jpg", requireContext())
            viewModel.uri = Uri.fromFile(viewModel.tempImgFile)
            helper()
            cropBtn.setOnClickListener {
                viewModel.cropImage(bmp, cropImageView)
            }
        } ?: run {
            helper()
            cropBtn.setOnClickListener {
                viewModel.cropImage(imagePreview.drawable.toBitmap(), cropImageView)
            }
        }
    }

    // Må ligge her; callback kan kanskje flyttes ut
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.getBmpFromCamera(requireContext(), cameraResultLauncher)
            }
        }

    // Må ligge her; callback kan kanskje flyttes ut
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(viewModel.tempImgFile.absolutePath)
                viewModel.uri = Uri.fromFile(viewModel.tempImgFile)
                imagePreview.setImageBitmap(bitmap)
//                searchBtn.visibility = View.VISIBLE
//                cropBtn.visibility = View.VISIBLE
            }
        }

    // Må ligge her; callback kan kanskje flyttes ut
    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                viewModel.uri = it.data?.data!!
                val bitmap =
                    BitmapUtils.getBitmap(requireContext(), null, viewModel.uri.toString(), ::UriToBitmap)
                imagePreview.setImageBitmap(bitmap)
//                searchBtn.visibility = View.VISIBLE
//                cropBtn.visibility = View.VISIBLE

            } else {
                println("error: result is not OK, or data is empty")
            }
        }
}
