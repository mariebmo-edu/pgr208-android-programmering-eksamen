package no.kristiania.reverseimagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.model.entity.ResultItem


// Denne klassen forteller recyclerview hvordan den skal vise data fra databasen.
class ResultItemAdapter : RecyclerView.Adapter<ResultItemAdapter.ResultItemViewHolder>() {

    var data = listOf<ResultItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    // Når den indre klassen under instansieres (dette fungerer som et rot-element for å stappe result_item xml-fila inn i.
    // Den blir inflatet i den indre klassen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ResultItemViewHolder = ResultItemViewHolder.inflateFrom(parent)

    // Denne kalles for hver gang en recyclerview blir opprettet eller brukt på nytt,
    // for å legge til data i viewet. Dette skjer også i den indre klassen under
    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    // denne klassen har ansvar for å legge til data i hvert result_item.xml som benyttes i recyclerviewet, samt å inflate de
    class ResultItemViewHolder(rootView: CardView) : RecyclerView.ViewHolder(rootView){
        // TODO: 06.04.2022
        //  variabler til bilde-view, eventuelt annen tekst. Trenger vel på sikt både thumbnail og mulighet til expand,
        //  ser for meg at vi har et egen fragment til fullscreen view av bilde men idk

        companion object {
            fun inflateFrom(parent: ViewGroup): ResultItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.result_item, parent, false) as CardView
                return ResultItemViewHolder(view)
            }
        }
        fun bind(resultItem: ResultItem) {
            // TODO: sett variablene fra to-do ^over^, til verdien av result item som kommer fra databasen og inn her
        }
    }


}