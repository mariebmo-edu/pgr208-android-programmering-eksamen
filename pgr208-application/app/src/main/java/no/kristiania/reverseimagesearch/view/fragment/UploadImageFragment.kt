package no.kristiania.reverseimagesearch.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.UploadImageFragmentBinding
import no.kristiania.reverseimagesearch.view.RequestImageDto
import no.kristiania.reverseimagesearch.viewmodel.UploadImageViewModel
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap
import no.kristiania.reverseimagesearch.viewmodel.utils.NetworkUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.ViewUtils
import java.io.File

class UploadImageFragment : Fragment() {

    private var _binding: UploadImageFragmentBinding? = null
    private val binding get() = _binding!!
    private var _viewModel: UploadImageViewModel? = null
    private val viewModel get() = _viewModel!!
    private lateinit var galleryBtn: Button
    private lateinit var cameraBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var cropImageView: CropImageView
    private var shouldGetBmp: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deactivateResultMenuItem()
        setHasOptionsMenu(true)
        _binding = UploadImageFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        _viewModel = ViewModelProvider(this)[UploadImageViewModel::class.java]

        binding.lifecycleOwner = viewLifecycleOwner
        galleryBtn = binding.galleryBtn
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

        galleryBtn.setOnClickListener {
            viewModel.pickImageGallery(galleryResultLauncher)
        }

        cameraBtn.setOnClickListener {
            viewModel.pickImageCamera(
                requireContext(),
                cameraResultLauncher,
                cameraPermissionRequest
            )
        }

        cropBtn.setOnClickListener {
            shouldGetBmp = true
            viewModel.cropImage(imagePreview.drawable.toBitmap(), cropImageView)
            shouldGetBmp = false
        }

        imagePreview.setOnClickListener {
            viewModel.uri.value?.let {
                val bitmap = UriToBitmap(requireContext(), null, it.toString())
                ViewUtils().fullSizeImage(bitmap, view.context)
            }
        }

        viewModel.uri.observe(viewLifecycleOwner, {
            it?.let {
                val bitmap =
                    BitmapUtils.getBitmap(requireContext(), null, it.toString(), ::UriToBitmap)
                imagePreview.setImageBitmap(bitmap)
                searchBtn.visibility = View.VISIBLE
                cropBtn.visibility = View.VISIBLE
            } ?: run {
                searchBtn.visibility = View.GONE
                cropBtn.visibility = View.GONE
            }
        })

        viewModel.url.observe(viewLifecycleOwner, { url ->
            if (viewModel.shouldNavigate) {
                val action = UploadImageFragmentDirections
                    .actionSearchFragmentToResultFragment(RequestImageDto(url = url, uri= viewModel.uri.value.toString()))
                this.findNavController().navigate(action)
                activateResultMenuItem()
                viewModel.shouldNavigate = false
            }
        })

        viewModel.cropping.observe(viewLifecycleOwner, {
            if (it) {
                cropOn()
            } else if (viewModel.uri.value != null && !it) {
                cropOff(shouldGetBmp)
            }
        })

        searchBtn.setOnClickListener {

            when (NetworkUtils().isConnected(requireContext())) {
                true -> {
                    viewModel.shouldNavigate = true
                    viewModel.uploadImageForUrl(imagePreview.drawable.toBitmap(), requireContext())
                }
                false -> Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG)
                    .show()
            }
        }

        return view
    }

    private fun activateResultMenuItem() {
        val appCompat = requireActivity() as AppCompatActivity
        val navigationView = appCompat.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val results = navigationView.menu.getItem(2)
        results.isEnabled = true
    }

    private fun deactivateResultMenuItem() {
        val appCompat = requireActivity() as AppCompatActivity
        val navigationView = appCompat.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val results = navigationView.menu.getItem(2)
        results.isEnabled = false
    }

    private fun cropOn() {
        imagePreview.visibility = View.GONE
        galleryBtn.visibility = View.GONE
        cameraBtn.visibility = View.GONE
        cropImageView.visibility = View.VISIBLE
        cropBtn.text = getString(R.string.finish_cropping)
        cropBtn.setOnClickListener {
            shouldGetBmp = true
            viewModel.finishCropping(cropImageView.croppedImage, imagePreview)
            shouldGetBmp = false
        }
        searchBtn.text = getString(R.string.back)
        searchBtn.setOnClickListener {
            viewModel.abortCropping()
            resetCrop()
        }
    }

    private fun resetCrop() {
        cropImageView.visibility = View.GONE
        imagePreview.visibility = View.VISIBLE
        galleryBtn.visibility = View.VISIBLE
        cameraBtn.visibility = View.VISIBLE
        cropBtn.text = getString(R.string.crop_image)
        searchBtn.text = getString(R.string.search)
        cropBtn.setOnClickListener {
            viewModel.cropImage(imagePreview.drawable.toBitmap(), cropImageView)
        }
        searchBtn.setOnClickListener {
            viewModel.shouldNavigate = true
            viewModel.uploadImageForUrl(imagePreview.drawable.toBitmap(), requireContext())
        }
    }

    private fun cropOff(shouldGetBmp: Boolean) {

        var bitmap: Bitmap? = null

        if (shouldGetBmp) {
            bitmap = cropImageView.croppedImage
        }

        bitmap?.let { bmp ->
            viewModel.tempImgFile.writeBytes(BitmapUtils.bitmapToByteArray(bmp))
            viewModel.setUri(Uri.fromFile(viewModel.tempImgFile))
            resetCrop()
        } ?: resetCrop()
    }


    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.getBmpFromCamera(requireContext(), cameraResultLauncher)
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(viewModel.tempImgFile.absolutePath)
                viewModel.setUri(Uri.fromFile(viewModel.tempImgFile))
                imagePreview.setImageBitmap(bitmap)
            }
        }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            fun printError() {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT)
            }

            if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                val uri = it.data!!.data
                uri?.let {
                    viewModel.setUri(uri)
                    val bitmap =
                        BitmapUtils.getBitmap(
                            requireContext(),
                            null,
                            viewModel.uri.value.toString(),
                            ::UriToBitmap
                        )
                    imagePreview.setImageBitmap(bitmap)
                } ?: printError()
            } else {
                printError()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
