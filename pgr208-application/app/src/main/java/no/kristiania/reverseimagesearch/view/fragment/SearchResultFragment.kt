package no.kristiania.reverseimagesearch.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import no.kristiania.reverseimagesearch.R
import no.kristiania.reverseimagesearch.databinding.SearchResultFragmentBinding
import no.kristiania.reverseimagesearch.model.controller.ResultController
import no.kristiania.reverseimagesearch.model.db.ImageSearchDb
import no.kristiania.reverseimagesearch.view.adapter.SearchResultItemAdapter
import no.kristiania.reverseimagesearch.viewmodel.SearchResultViewModel
import no.kristiania.reverseimagesearch.viewmodel.api.FastNetworkingAPI
import no.kristiania.reverseimagesearch.viewmodel.factory.SearchResultViewModelFactory

class SearchResultFragment : Fragment() {

    private var _binding: SearchResultFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Dette er "dataBinding/ViewBinding. Erstatter findById og er mer minne-effektiv
        _binding = SearchResultFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        val api = context?.let { FastNetworkingAPI(it) }


        val application = requireNotNull(this.activity).application
        val db = ImageSearchDb.getInstance(application)
        val requestImageDao = db.requestImageDao
        val resultImageDao = db.resultImageDao
        val resultController = ResultController(resultImageDao, requestImageDao)
        val resultViewModelFactory = SearchResultViewModelFactory(resultController)
        val viewModel =
            ViewModelProvider(this, resultViewModelFactory)[SearchResultViewModel::class.java]
        viewModel.hostedImageServerUrl =
            SearchResultFragmentArgs.fromBundle(requireArguments()).responseUrl
        viewModel.requestImageLocalPath =
            SearchResultFragmentArgs.fromBundle(requireArguments()).requestImagePath
        Log.d("ResultFragment", viewModel.hostedImageServerUrl)


        // One of many callbacks in the app
        viewModel.shouldSearch.observe(viewLifecycleOwner, { shouldSearch ->
            if (shouldSearch && api != null) {
                viewModel.getResultFromUrl(viewModel.hostedImageServerUrl, api)
            }
        })


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = SearchResultItemAdapter()
        // app.req 1
        binding.resultItemsList.adapter = adapter

        var i = 0
        var timer = true
        // callback
        viewModel.resultImages.observe(viewLifecycleOwner, {

            Log.d("SHOULD_SEARCH", viewModel.shouldSearch.value.toString())
            if (viewModel.shouldSearch.value!!) {
                toggleViews(true, view)
            } else {
                viewModel.searchDone()
                toggleViews(false, view)
                timer = false
            }

            Log.i("ResultFragment", "Submitting list")
            it?.let {
                adapter.submitList(it)
                viewModel.setInfoText("${i++}/3 results added")
            }

            //Turns off the loading bar if there is a timeout - 20 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (timer) {
                    toggleViews(false, view)
                    Log.i("RESPONSE_TIMEOUT", "The response from the server took too long.")
                    viewModel.setInfoText("Response Timeout")
                    viewModel.searchDone()
                    timer = false
                }
            }, 20000)


        })
        // callback
        viewModel.shouldNavigateToSaved.observe(viewLifecycleOwner, {
            if (it) {
                val action =
                    SearchResultFragmentDirections.actionResultFragmentToSavedSearchesFragment()
                this.findNavController().navigate(action)
                viewModel.toggleNavigateToSaved()
            }
        })

        // callback
        binding.saveResultButton.setOnClickListener {
            val dialogueBuilder = AlertDialog.Builder(context)
            val popUpView = layoutInflater.inflate(R.layout.popup_fragment, null)

            val selectedName =
                popUpView.findViewById<AutoCompleteTextView>(R.id.autoCompleteCollectionNameTextView).text
            val submitBtn = popUpView.findViewById<Button>(R.id.submit_btn)
            val cancelBtn = popUpView.findViewById<Button>(R.id.cancel_btn)

            dialogueBuilder.setView(popUpView)
            val dialog = dialogueBuilder.create()
            dialog.show()

            submitBtn.setOnClickListener {
                viewModel.saveResult(
                    requireContext(),
                    adapter.selectedImagesForSave,
                    selectedName.toString()
                )
                dialog.dismiss()
                Toast.makeText(context, "Collection Saved!", Toast.LENGTH_LONG).show()
                viewModel.toggleNavigateToSaved()
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

    fun toggleViews(loading: Boolean, view: View) {

        when (loading) {
            true -> {
                view.findViewById<RelativeLayout>(R.id.loading_panel).visibility = View.VISIBLE
                view.findViewById<Button>(R.id.save_result_button).visibility = View.GONE
            }
            false -> {
                view.findViewById<RelativeLayout>(R.id.loading_panel).visibility = View.GONE
                view.findViewById<Button>(R.id.save_result_button).visibility = View.VISIBLE
            }
        }

    }

}