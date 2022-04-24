package no.kristiania.reverseimagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.databinding.CollectionItemBinding
import no.kristiania.reverseimagesearch.model.entity.RequestImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils

// app.req 1
// sub.req 4
class CollectionsAdapter(val clickListener: (id: Long, collectionName: String) -> Unit) :
    ListAdapter<RequestImage, CollectionsAdapter.SavedSearchItemViewHolder>(
        CollectionsDiffItemCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedSearchItemViewHolder = SavedSearchItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: SavedSearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class SavedSearchItemViewHolder(val binding: CollectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): SavedSearchItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CollectionItemBinding.inflate(layoutInflater, parent, false)
                return SavedSearchItemViewHolder(binding)
            }
        }

        fun bind(
            requestImage: RequestImage,
            clickListener: (id: Long, collectionName: String) -> Unit
        ) {
            requestImage.data?.let { it ->
                val bitmapImage = BitmapUtils.byteArrayToBitmap(it)
                binding.savedSearchImage.setImageBitmap(bitmapImage)
                binding.savedSearchText.text = requestImage.collectionName.toString()
                requestImage.id?.let { id ->
                    binding.root.setOnClickListener {
                        clickListener(
                            id,
                            requestImage.collectionName.toString()
                        )
                    }
                }
            }

        }
    }
}