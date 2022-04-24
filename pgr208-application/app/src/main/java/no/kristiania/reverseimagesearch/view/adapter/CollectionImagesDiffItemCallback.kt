package no.kristiania.reverseimagesearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class CollectionImagesDiffItemCallback : DiffUtil.ItemCallback<ResultImage>() {
    override fun areItemsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean =
        (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean =
        (oldItem == newItem)
}