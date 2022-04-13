package no.kristiania.reverseimagesearch

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    }


}