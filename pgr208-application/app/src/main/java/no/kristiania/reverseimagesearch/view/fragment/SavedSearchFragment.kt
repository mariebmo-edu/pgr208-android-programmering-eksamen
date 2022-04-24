package no.kristiania.reverseimagesearch.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import no.kristiania.reverseimagesearch.databinding.SavedSearchFragmentBinding
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.model.db.ResultImageDao
import no.kristiania.reverseimagesearch.view.adapter.SavedSearchAdapter
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchesViewModel
import no.kristiania.reverseimagesearch.viewmodel.factory.SavedSearchesViewModelFactory

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
        val adapter = SavedSearchAdapter { id, collectionName ->
            viewModel.onRequestClicked(id, collectionName)
        }

        binding.savedSearchList.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToResults.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("Navigate to results observer", "Navigating if not null")
                val action = SavedSearchFragmentDirections.actionSavedSearchesFragmentToSavedSearchesResultFragment(it,viewModel.collectionName!! )
                findNavController().navigate(action)
                viewModel.onNavigated()
            }
        })

        viewModel.savedSearchImages.observe(viewLifecycleOwner, {
            it?.let {
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