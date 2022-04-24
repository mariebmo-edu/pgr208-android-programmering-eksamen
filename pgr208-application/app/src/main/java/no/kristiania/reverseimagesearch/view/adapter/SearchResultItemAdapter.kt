package no.kristiania.reverseimagesearch.view.adapter

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.reverseimagesearch.databinding.SearchResultItemBinding
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.ViewUtils

// app.req 1
// sub.req 4
class SearchResultItemAdapter :
    ListAdapter<ResultImage, SearchResultItemAdapter.ResultItemViewHolder>(
        SearchResultDiffItemCallback()
    ) {
    val selectedImagesForSave = mutableListOf<ResultImage>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultItemViewHolder =
        ResultItemViewHolder.inflateFrom(parent)


    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        Log.i("onBind", "binding item")
        val item = getItem(position)
        val image = holder.binding.image

        image.setOnClickListener {
            ViewUtils().fullSizeImage((image.drawable as BitmapDrawable).bitmap, it.context)
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

    class ResultItemViewHolder(val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ResultItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchResultItemBinding.inflate(layoutInflater, parent, false)
                return ResultItemViewHolder(binding)
            }
        }

        fun bind(resultImage: ResultImage) {
            Log.i("Load image", resultImage.toString())

            Glide.with(binding.root)
                .load(resultImage.serverPath)
                .into(binding.image)

        }
    }
}
