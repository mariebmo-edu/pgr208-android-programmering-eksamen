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
import no.kristiania.reverseimagesearch.viewmodel.utils.BitmapUtils

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

        /* TODO:
        *   * Do call to other api-s with hostedImageServer url
        *   * Process JSON to a List<ResultImage> entity
        *   * Set result to viewmodel (probably need to change the field to public and get rid of getter)
        *   * ViewModel should update recyclerview automatically
        * */

        val application = requireNotNull(this.activity).application
        val dao = ImageSearchDb.getInstance(application).requestImageDao

        val resultViewModelFactory = ResultViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, resultViewModelFactory)[ResultViewModel::class.java]

        api.getImageFromProvider(hostedImageServerUrl, FastNetworkingAPI.ImageProvider.Bing, viewModel)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}