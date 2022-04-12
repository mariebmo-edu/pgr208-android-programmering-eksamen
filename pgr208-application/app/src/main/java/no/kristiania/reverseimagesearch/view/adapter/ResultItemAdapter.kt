package no.kristiania.reverseimagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.databinding.ResultItemBinding
import no.kristiania.reverseimagesearch.model.entity.ResultImage


// Denne klassen forteller recyclerview hvordan den skal vise data fra databasen.
class ResultItemAdapter : ListAdapter<ResultImage, ResultItemAdapter.ResultItemViewHolder>(ResultDiffItemCallback()) {

    // Når den indre klassen under instansieres (dette fungerer som et rot-element for å stappe result_item xml-fila inn i.
    // Den blir inflatet i den indre klassen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ResultItemViewHolder = ResultItemViewHolder.inflateFrom(parent)

    // Denne kalles for hver gang en recyclerview blir opprettet eller brukt på nytt,
    // for å legge til data i viewet. Dette skjer også i den indre klassen under
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    // denne klassen har ansvar for å legge til data i hvert result_item.xml som benyttes i recyclerviewet, samt å inflate de
    class ResultItemViewHolder(val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun inflateFrom(parent: ViewGroup): ResultItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ResultItemBinding.inflate(layoutInflater, parent, false)
                return ResultItemViewHolder(binding)
            }
        }
        fun bind(resultImage: ResultImage) {
           binding.resultItem = resultImage
        }
    }


}