package no.kristiania.reverseimagesearch.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import no.kristiania.reverseimagesearch.databinding.CollectionsFragmentBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.CollectionsAdapter
import no.kristiania.reverseimagesearch.viewmodel.CollectionsViewModel
import no.kristiania.reverseimagesearch.viewmodel.factory.CollectionsViewModelFactory

class CollectionsFragment : Fragment() {

    private lateinit var viewModel: CollectionsViewModel
    private var _binding: CollectionsFragmentBinding? = null
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
            CollectionsViewModelFactory(requestImageDao)
        )[CollectionsViewModel::class.java]
        _binding = CollectionsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = CollectionsAdapter { id, collectionName ->
            viewModel.onRequestClicked(id, collectionName)
        }


        binding.savedSearchList.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToResults.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("Navigate to results observer", "Navigating if not null")
                val action =
                    CollectionsFragmentDirections.actionSavedSearchesFragmentToSavedSearchesResultFragment(
                        it,
                        viewModel.collectionName!!
                    )
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