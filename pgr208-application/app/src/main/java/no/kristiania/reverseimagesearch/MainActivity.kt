package no.kristiania.reverseimagesearch

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import no.kristiania.reverseimagesearch.view.fragment.ImageSearchFragment
import no.kristiania.reverseimagesearch.view.fragment.NothingSelectedFragment

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


}