package no.kristiania.reverseimagesearch.viewmodel.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import no.kristiania.reverseimagesearch.R


class ViewUtils {

    fun fullSizeImage(bitmap: Bitmap, context: Context){


        val dialogueBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val imageViewer = inflater.inflate(R.layout.fragment_fullscreen_image, null)

        val photoViewContainer = imageViewer.findViewById<ConstraintLayout>(R.id.photo_view_constraint)
        val photoView = imageViewer.findViewById<PhotoView>(R.id.photo_view)
        val closeBtn = imageViewer.findViewById<FloatingActionButton>(R.id.photo_view_close)


        photoView.setImageBitmap(bitmap)
        photoViewContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.lm_background_color))

        dialogueBuilder.setView(imageViewer)
        val showImageFullscreen = dialogueBuilder.create()

        closeBtn.setOnClickListener {
            showImageFullscreen.dismiss()
        }
    }


}

/*
val dialogueBuilder = AlertDialog.Builder(context)
            val popUpView = layoutInflater.inflate(R.layout.fragment_popup__text_w_button, null)

            val selectedName =
                popUpView.findViewById<AutoCompleteTextView>(R.id.autoCompleteCollectionNameTextView).text
            val submitBtn = popUpView.findViewById<Button>(R.id.submit_btn)
            val cancelBtn = popUpView.findViewById<Button>(R.id.cancel_btn)

            dialogueBuilder.setView(popUpView)
            val dialog = dialogueBuilder.create()
            dialog.show()

            submitBtn.setOnClickListener {
                viewModel.saveResult(requireContext(), adapter.selectedImagesForSave, selectedName.toString())
                dialog.dismiss()
                Toast.makeText(context, "Collection Saved!", Toast.LENGTH_LONG).show()
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
 */