package no.kristiania.reverseimagesearch

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import no.kristiania.reverseimagesearch.viewmodel.api.Http
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap


class ImageSearchFragment : Fragment() {

    private lateinit var searchBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var selectedImage: Bitmap
    private  var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            uri = requireArguments().getParcelable(IMG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_search, container, false)

        searchBtn = view.findViewById<Button>(R.id.search_btn)
        imagePreview = view.findViewById(R.id.uploaded_image)

        if(uri != null){
            selectedImage = BitmapUtils.getBitmap(requireContext(), null, uri.toString(), ::UriToBitmap)
            imagePreview.setImageBitmap(selectedImage)
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

        searchBtn.setOnClickListener{
            uploadImageToServer(selectedImage)
        }
    }

    private fun uploadImageToServer(bitmap : Bitmap) {

        val http = Http()

        val response = context?.let { http.uploadImage(bitmap, it) }
        println("response: $response")
    }
}