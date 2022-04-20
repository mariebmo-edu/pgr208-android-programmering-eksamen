package no.kristiania.reverseimagesearch.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.databinding.SavedSearchResultsItemBinding
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils

class SavedSearchResultsAdapter : ListAdapter<ResultImage, SavedSearchResultsAdapter.SavedSearchResultsItemViewHolder>(SavedSearchResultsDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedSearchResultsItemViewHolder {
        Log.d("onCreate", "Inflating")
        return SavedSearchResultsItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: SavedSearchResultsItemViewHolder, position: Int) {
        val resultImage = getItem(position)
        Log.i("Load image", "Loading result image in binding")

        holder.bind(resultImage)
    }

    class SavedSearchResultsItemViewHolder(val binding: SavedSearchResultsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): SavedSearchResultsItemViewHolder {
                Log.d("inflater", "Inflating!")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SavedSearchResultsItemBinding.inflate(layoutInflater, parent, false)
                return SavedSearchResultsItemViewHolder(binding)
            }
        }

        fun bind(resultImage: ResultImage) {
            resultImage.data?.let {
                val bitMapImage = BitmapUtils.byteArrayToBitmap(it)
                binding.savedSearchResultImage.setImageBitmap(bitMapImage)
            }
        }
    }
}