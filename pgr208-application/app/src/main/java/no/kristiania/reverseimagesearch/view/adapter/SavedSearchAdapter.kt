package no.kristiania.reverseimagesearch.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.reverseimagesearch.databinding.ResultItemBinding
import no.kristiania.reverseimagesearch.databinding.SavedSearchItemBinding
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils

class SavedSearchAdapter :
    ListAdapter<RequestImage, SavedSearchAdapter.SavedSearchItemViewHolder>(
        SavedSearchDiffItemCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedSearchItemViewHolder = SavedSearchItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: SavedSearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SavedSearchItemViewHolder(val binding: SavedSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): SavedSearchItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SavedSearchItemBinding.inflate(layoutInflater, parent, false)
                return SavedSearchItemViewHolder(binding)
            }
        }

        fun bind(requestImage: RequestImage) {
            requestImage.data?.let {
                val bitmapImage = BitmapUtils.byteArrayToBitmap(it)
                binding.savedResultImage.setImageBitmap(bitmapImage)
            }
            binding.textToBeDeleted.text = "faahk"
            Log.i("Load image", "Loading image in binding")
        }
    }
}