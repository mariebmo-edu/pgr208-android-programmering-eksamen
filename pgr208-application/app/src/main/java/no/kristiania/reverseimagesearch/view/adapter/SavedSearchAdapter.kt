package no.kristiania.reverseimagesearch.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.databinding.SavedSearchItemBinding
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.ViewUtils

class SavedSearchAdapter(val clickListener: (id: Long) -> Unit) :
    ListAdapter<RequestImage, SavedSearchAdapter.SavedSearchItemViewHolder>(
        SavedSearchDiffItemCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedSearchItemViewHolder = SavedSearchItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: SavedSearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
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

        fun bind(requestImage: RequestImage, clickListener: (id: Long) -> Unit) {
            requestImage.data?.let { it ->
                val bitmapImage = BitmapUtils.byteArrayToBitmap(it)
                binding.savedSearchImage.setImageBitmap(bitmapImage)
                binding.savedSearchText.text = requestImage.collectionName.toString()
                binding.savedSearchImage.setOnClickListener {}
                requestImage.id?.let { id ->
                    binding.root.setOnClickListener { clickListener(id) }
                }
            }
            Log.i("Load image", "Loading image in binding")
        }
    }
}