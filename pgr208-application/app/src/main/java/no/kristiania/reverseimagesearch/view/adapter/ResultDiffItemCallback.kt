package no.kristiania.reverseimagesearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import no.kristiania.reverseimagesearch.model.entity.ResultImage


// Sammenlikner to lister med ResultItem objekter. Brukes for å oppdatere databindingen om noe endrer seg
class ResultDiffItemCallback : DiffUtil.ItemCallback<ResultImage>() {
    override fun areItemsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: ResultImage, newItem: ResultImage): Boolean = (oldItem == newItem)
}