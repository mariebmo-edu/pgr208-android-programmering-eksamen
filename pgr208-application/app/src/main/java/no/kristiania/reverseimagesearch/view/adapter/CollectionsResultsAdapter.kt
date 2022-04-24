package no.kristiania.reverseimagesearch.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.databinding.CollectionImageItemBinding
import no.kristiania.reverseimagesearch.model.entity.ResultImage
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils
import no.kristiania.reverseimagesearch.viewmodel.utils.ViewUtils

class CollectionsResultsAdapter :
    ListAdapter<ResultImage, CollectionsResultsAdapter.CollectionImagesItemViewHolder>(
        CollectionImagesDiffItemCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionImagesItemViewHolder {
        Log.d("onCreate", "Inflating")
        return CollectionImagesItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: CollectionImagesItemViewHolder, position: Int) {
        val resultImage = getItem(position)
        Log.i("Load image", "Loading result image in binding")

        holder.bind(resultImage)
    }

    class CollectionImagesItemViewHolder(val binding: CollectionImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CollectionImagesItemViewHolder {
                Log.d("inflater", "Inflating!")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CollectionImageItemBinding.inflate(layoutInflater, parent, false)
                return CollectionImagesItemViewHolder(binding)
            }
        }

        fun bind(resultImage: ResultImage) {
            resultImage.data?.let {
                val bitMapImage = BitmapUtils.byteArrayToBitmap(it)
                binding.savedSearchResultImage.setImageBitmap(bitMapImage)
                binding.savedSearchResultImage.setOnClickListener {
                    ViewUtils().fullSizeImage(bitMapImage, binding.root.context)
                }
            }
        }
    }
}