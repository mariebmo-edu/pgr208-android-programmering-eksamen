package no.kristiania.reverseimagesearch.viewmodel.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.chrisbanes.photoview.PhotoView
import no.kristiania.reverseimagesearch.R

class ViewUtils {

    fun showPopUpWithInputAndSubmit(hintText : String?, buttonText : String?){

    }

    fun fullSizeImage(bitmap: Bitmap, view: View, context: Context){

        val photoViewContainer = view.findViewById<ConstraintLayout>(R.id.photo_view_constraint)
        val photoView = view.findViewById<PhotoView>(R.id.photo_view)
        photoView.setImageBitmap(bitmap)
        photoViewContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.lm_background_color))
        photoViewContainer.visibility = View.VISIBLE

        val closeBtn = view.findViewById<Button>(R.id.photo_view_close)

        closeBtn.setOnClickListener {
            photoViewContainer.visibility = View.INVISIBLE
        }
    }


}