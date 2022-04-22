package no.kristiania.reverseimagesearch.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import no.kristiania.reverseimagesearch.SavedSearchResultsFragmentDirections
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchesViewModel
import no.kristiania.reverseimagesearch.viewmodel.factory.SavedSearchesViewModelFactory
import no.kristiania.reverseimagesearch.databinding.SavedSearchFragmentBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.SavedSearchAdapter

class SavedSearchFragment : Fragment() {

    private lateinit var viewModel: SavedSearchesViewModel
    private var _binding: SavedSearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val requestImageDao = db.requestImageDao
        viewModel = ViewModelProvider(
            this,
            SavedSearchesViewModelFactory(requestImageDao)
        )[SavedSearchesViewModel::class.java]
        _binding = SavedSearchFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = SavedSearchAdapter { id ->
            viewModel.onRequestClicked(id)
        }

        binding.savedSearchList.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.navigateToResults.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("Navigate to results observer", "Navigating if not null")
                val action = SavedSearchFragmentDirections.actionSavedSearchesFragmentToSavedSearchesResultFragment(it)
                findNavController().navigate(action)
                viewModel.onNavigated()
            }
        })

        viewModel.savedSearchImages.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("obeserving list", it.size.toString())
                adapter.submitList(it)
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}