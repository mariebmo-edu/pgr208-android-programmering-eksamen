package no.kristiania.reverseimagesearch.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import no.kristiania.reverseimagesearch.viewmodel.ResultViewModel
import no.kristiania.reverseimagesearch.databinding.FragmentResultBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.ResultItemAdapter
import no.kristiania.reverseimagesearch.viewmodel.ResultViewModelFactory
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Dette er "dataBinding/ViewBinding. Erstatter findById og er mer minne-effektiv
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val view = binding.root

        val hostedImageServerUrl = ResultFragmentArgs.fromBundle(requireArguments()).responseUrl
        val api = FastNetworkingAPI()
        Log.d("ResultFragment", hostedImageServerUrl)


        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val requestImageDao = db.requestImageDao
        val resultImageDao = db.resultImageDao

        val resultViewModelFactory = ResultViewModelFactory(requestImageDao, resultImageDao)
        val viewModel = ViewModelProvider(this, resultViewModelFactory)[ResultViewModel::class.java]

        api.getImageFromProvider(hostedImageServerUrl, FastNetworkingAPI.ImageProvider.Bing, viewModel)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //binding.resultItemsList.
        // Til databinding med livedata
        val adapter = ResultItemAdapter()
        binding.resultItemsList.adapter = adapter
        // Observer endringer i view modellens liste av resultitems
        viewModel.resultImages.observe(viewLifecycleOwner, Observer {
            Log.i("ResultFragment", "Submitting list")
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.saveResultButton.setOnClickListener {
            Log.d("Button Clicked!", adapter.selectedImagesForSave.toString())

            // TODO: Get bitmap from URI param
            // Create instance of requestImage
            // Save it
            // update ID on all selected resultItem(s)
            // Save them
            // ??
            // Profit

        }



        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}