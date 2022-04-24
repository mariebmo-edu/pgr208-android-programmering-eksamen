package no.kristiania.reverseimagesearch.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchResultsViewModel
import no.kristiania.reverseimagesearch.databinding.SavedSearchResultsFragmentBinding
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.SavedSearchResultsAdapter
import no.kristiania.reverseimagesearch.viewmodel.factory.SavedSearchResultsViewModelFactory

class SavedSearchResultsFragment : Fragment() {


    private var _binding: SavedSearchResultsFragmentBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: SavedSearchResultsViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val requestImageId = SavedSearchResultsFragmentArgs.fromBundle(requireArguments()).requestId
        val collectionName = SavedSearchResultsFragmentArgs.fromBundle(requireArguments()).collectionName

        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val dao = db.resultImageDao
        _binding = SavedSearchResultsFragmentBinding.inflate(inflater,container,false)
        _viewModel = ViewModelProvider(this, SavedSearchResultsViewModelFactory(dao,requestImageId))[SavedSearchResultsViewModel::class.java]

        val view = binding.root

        val adapter = SavedSearchResultsAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.savedSearchResultsList.adapter = adapter
        binding.viewModel = viewModel


        viewModel.resultImages.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("observer", "Submitting list ${it.size}")
                adapter.submitList(it)
                viewModel.setCollectionName(collectionName)
            }
        })


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _viewModel = null
    }
}