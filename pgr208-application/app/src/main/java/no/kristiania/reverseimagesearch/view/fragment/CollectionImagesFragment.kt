package no.kristiania.reverseimagesearch.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.databinding.CollectionImagesFragmentBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.CollectionsResultsAdapter
import no.kristiania.reverseimagesearch.viewmodel.CollectionImagesViewModel
import no.kristiania.reverseimagesearch.viewmodel.factory.CollectionImagesViewModelFactory

class CollectionImagesFragment : Fragment() {


    private var _binding: CollectionImagesFragmentBinding? = null
    private val binding get() = _binding!!

    private var _viewModel: CollectionImagesViewModel? = null
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val requestImageId = CollectionImagesFragmentArgs.fromBundle(requireArguments()).requestId
        val collectionName =
            CollectionImagesFragmentArgs.fromBundle(requireArguments()).collectionName

        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val dao = db.resultImageDao
        _binding = CollectionImagesFragmentBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(
            this,
            CollectionImagesViewModelFactory(dao, requestImageId)
        )[CollectionImagesViewModel::class.java]

        val view = binding.root
        // app.req 1
        val adapter = CollectionsResultsAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.savedSearchResultsList.adapter = adapter
        binding.viewModel = viewModel

        // callback, lambda
        viewModel.resultImages.observe(viewLifecycleOwner, {
            it?.let {
                Log.d("observer", "Submitting list ${it.size}")
                adapter.submitList(it)
                viewModel.setCollectionName(collectionName)
                activity?.title = viewModel.collectionName.value
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