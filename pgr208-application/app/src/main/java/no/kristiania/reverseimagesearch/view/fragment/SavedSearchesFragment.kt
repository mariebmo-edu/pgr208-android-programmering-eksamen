package no.kristiania.reverseimagesearch.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchesViewModel

class SavedSearchesFragment : Fragment() {

    companion object {
        fun newInstance() = SavedSearchesFragment()
    }

    private lateinit var viewModel: SavedSearchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_searches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavedSearchesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}