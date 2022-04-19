package no.kristiania.reverseimagesearch.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.kristiania.reverseimagesearch.databinding.SavedSearchesFragmentBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.SavedSearchItemAdapter
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchesViewModel
import no.kristiania.reverseimagesearch.viewmodel.SavedSearchesViewModelFactory

class SavedSearchesFragment : Fragment() {
    private var _binding: SavedSearchesFragmentBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: SavedSearchesViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavedSearchesFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val dao =
            ImageSearchDb.getInstance(requireNotNull(this.activity).application).requestImageDao

        val viewModelFactory = SavedSearchesViewModelFactory(dao)
        _viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SavedSearchesViewModel::class.java]

        val adapter = SavedSearchItemAdapter()
        binding.savedRequestsList.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.requestImages.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("SavedSearchesFragment", "Submitting list!")
                Log.d("SavedSearchesFragment", it.toString())
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