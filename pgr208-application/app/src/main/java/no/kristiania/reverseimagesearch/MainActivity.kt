package no.kristiania.reverseimagesearch

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import no.kristiania.reverseimagesearch.viewmodel.api.Http
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils.Companion.UriToBitmap
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var savedBtn: Button
    private lateinit var uploadBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedBtn = findViewById(R.id.saved_btn)
        uploadBtn = findViewById(R.id.upload_btn)

        uploadBtn.setOnClickListener {
            pickImageGallery()
        }

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragmentContainerView, NothingSelectedFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher  =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {

                supportFragmentManager.beginTransaction().apply{
                    replace(R.id.fragmentContainerView, ImageSearchFragment.newInstance(it.data?.data!!))
                    addToBackStack(null)
                    commit()
                }

            } else {
                println("error: result is not OK, or data is empty")
            }
        }
}