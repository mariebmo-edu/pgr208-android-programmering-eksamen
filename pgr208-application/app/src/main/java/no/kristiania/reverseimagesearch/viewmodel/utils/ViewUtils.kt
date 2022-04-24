package no.kristiania.reverseimagesearch.viewmodel.utils

import android.app.ActionBar
import android.content.Context
import android.graphics.Bitmap
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.PopupWindow
import com.github.chrisbanes.photoview.PhotoView

import no.kristiania.reverseimagesearch.R


class ViewUtils {

    fun fullSizeImage(bitmap: Bitmap, context: Context) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val imageViewer = inflater.inflate(R.layout.fullscreen_image_fragment, null)

        val photoView = imageViewer.findViewById<PhotoView>(R.id.photo_view)
        val closeBtn = imageViewer.findViewById<Button>(R.id.photo_view_close)

        photoView.setImageBitmap(bitmap)

        val popUpWindow = PopupWindow(
            imageViewer,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )

        closeBtn.setOnClickListener {
            popUpWindow.dismiss()
        }

        popUpWindow.showAtLocation(imageViewer, Gravity.CENTER, 0, 0)
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