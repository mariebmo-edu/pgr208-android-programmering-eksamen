package no.kristiania.reverseimagesearch

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import no.kristiania.reverseimagesearch.viewmodel.api.Http
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var searchBtn: Button
    private lateinit var uploadBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var selectedImage: File


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBtn = findViewById(R.id.search_btn)
        uploadBtn = findViewById(R.id.upload_btn)
        imagePreview = findViewById(R.id.uploaded_image)
        uploadBtn.setOnClickListener {
            pickImageGallery()
        }

        searchBtn.setOnClickListener{
            uploadImageToServer(selectedImage)
        }
    }

    private fun uploadImageToServer(image : File?) {

        val http = Http()

        if(image != null){
            val response = http.uploadImage(image)
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)

    }

    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imagePreview.setImageURI(result.data?.data)
                selectedImage = File((result.data?.data)!!.path!!)
            } else {
                println("U ARE FUKD")
            }
        }
}