package no.kristiania.reverseimagesearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import no.kristiania.reverseimagesearch.model.entity.ResultItem


// Sammenlikner to lister med ResultItem objekter. Brukes for å oppdatere databindingen om noe endrer seg
class ResultDiffItemCallback : DiffUtil.ItemCallback<ResultItem>() {
    override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean = (oldItem == newItem)
}