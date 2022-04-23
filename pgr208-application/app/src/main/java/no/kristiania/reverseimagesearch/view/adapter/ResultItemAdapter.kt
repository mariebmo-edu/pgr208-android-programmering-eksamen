package no.kristiania.reverseimagesearch.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.ResultItemBinding
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.ViewUtils
import java.util.logging.Level.INFO
import kotlin.coroutines.coroutineContext


// Denne klassen forteller recyclerview hvordan den skal vise data fra databasen.
class ResultItemAdapter :
    ListAdapter<ResultImage, ResultItemAdapter.ResultItemViewHolder>(ResultDiffItemCallback()) {
    val selectedImagesForSave = mutableListOf<ResultImage>()


    // Når den indre klassen under instansieres (dette fungerer som et rot-element for å stappe result_item xml-fila inn i.
    // Den blir inflatet i den indre klassen
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultItemViewHolder =
        ResultItemViewHolder.inflateFrom(parent)


    // Denne kalles for hver gang en recyclerview blir opprettet eller brukt på nytt,
    // for å legge til data i viewet. Dette skjer også i den indre klassen under
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        Log.i("onBind", "binding item")
        val item = getItem(position)
        val image = holder.binding.image

        image.setOnClickListener {
            Log.d("IMAGE_CLICKED", "Image $position was clicked")
            ViewUtils().fullSizeImage((image.drawable as BitmapDrawable).bitmap, it.context.applicationContext)
        }

        holder.binding.saveResult.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                item.data = BitmapUtils.bitmapToByteArray((image.drawable as BitmapDrawable).bitmap)
                selectedImagesForSave.add(item)
            } else if (!isChecked) {
                selectedImagesForSave.remove(item)
            }
        }
        holder.bind(item)
    }

    // denne klassen har ansvar for å legge til data i hvert result_item.xml som benyttes i recyclerviewet, samt å inflate de
    class ResultItemViewHolder(val binding: ResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ResultItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ResultItemBinding.inflate(layoutInflater, parent, false)
                return ResultItemViewHolder(binding)
            }
        }

        fun bind(resultImage: ResultImage) {
            Log.i("Load image", resultImage.toString())

            Glide.with(binding.root)
                .load(resultImage.serverPath)
                .into(binding.image)
            //binding.resultItem = resultImage
        }
    }


}

/*
StfalconImageViewer.Builder<Image>(context, images) { view, image ->
    Picasso.get().load(image.url).into(view)
}.show()
 */