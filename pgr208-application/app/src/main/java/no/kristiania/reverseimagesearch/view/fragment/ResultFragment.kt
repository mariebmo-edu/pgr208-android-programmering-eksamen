package no.kristiania.reverseimagesearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.viewmodel.ResultViewModel
import no.kristiania.reverseimagesearch.databinding.FragmentResultBinding
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.ResultItemAdapter
import no.kristiania.reverseimagesearch.viewmodel.factory.ResultViewModelFactory
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


        val api = context?.let { FastNetworkingAPI(it) }


        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val requestImageDao = db.requestImageDao
        val resultImageDao = db.resultImageDao
        val resultViewModelFactory = ResultViewModelFactory(requestImageDao, resultImageDao)
        val viewModel = ViewModelProvider(this, resultViewModelFactory)[ResultViewModel::class.java]
        viewModel.hostedImageServerUrl =
            ResultFragmentArgs.fromBundle(requireArguments()).responseUrl
        viewModel.requestImageLocalPath =
            ResultFragmentArgs.fromBundle(requireArguments()).requestImagePath
        Log.d("ResultFragment", viewModel.hostedImageServerUrl)


        viewModel.shouldSearch.observe(viewLifecycleOwner, { shouldSearch ->
            if (shouldSearch && api != null) {
                viewModel.getResultFromUrl(viewModel.hostedImageServerUrl, api)
                viewModel.searchDone()
            }
        })


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        //binding.resultItemsList.
        // Til databinding med livedata
        val adapter = ResultItemAdapter()
        binding.resultItemsList.adapter = adapter
        // Observer endringer i view modellens liste av resultitems

        var i = 0
        viewModel.resultImages.observe(viewLifecycleOwner, Observer {
            Log.i("ResultFragment", "Submitting list")
            it?.let {
                adapter.submitList(it)
                Toast.makeText(context, "${i++}/3 results added", Toast.LENGTH_SHORT).show()
            }
            if (view.findViewById<RecyclerView>(R.id.result_items_list).size > 0) {
                view.findViewById<RelativeLayout>(R.id.loading_panel).visibility = View.GONE
            }
        })

        binding.saveResultButton.setOnClickListener {
            val dialogueBuilder = AlertDialog.Builder(context)
            val popUpView = layoutInflater.inflate(R.layout.fragment_popup__text_w_button, null)

            val selectedName =
                popUpView.findViewById<AutoCompleteTextView>(R.id.autoCompleteCollectionNameTextView).text
            val submitBtn = popUpView.findViewById<Button>(R.id.submit_btn)
            val cancelBtn = popUpView.findViewById<Button>(R.id.cancel_btn)

            dialogueBuilder.setView(popUpView)
            val dialog = dialogueBuilder.create()
            dialog.show()

            submitBtn.setOnClickListener {
                viewModel.saveResult(requireContext(), adapter.selectedImagesForSave, selectedName.toString())
                dialog.dismiss()
                Toast.makeText(context, "Collection Saved!", Toast.LENGTH_LONG).show()
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}