package no.kristiania.reverseimagesearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import no.kristiania.reverseimagesearch.model.entity.RequestImage

class SavedSearchDiffItemCallback: DiffUtil.ItemCallback<RequestImage>() {
    override fun areItemsTheSame(oldItem: RequestImage, newItem: RequestImage): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: RequestImage, newItem: RequestImage): Boolean = (oldItem == newItem)
}