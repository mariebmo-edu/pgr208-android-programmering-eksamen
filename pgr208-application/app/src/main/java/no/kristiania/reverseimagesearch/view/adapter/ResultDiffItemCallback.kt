package no.kristiania.reverseimagesearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import no.kristiania.reverseimagesearch.model.entity.ResultImage

class ResultDiffItemCallback : DiffUtil.ItemCallback<ResultImage>() {
    override fun areItemsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean = (oldItem.serverPath == newItem.serverPath)

    override fun areContentsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean = (oldItem == newItem)
}