package com.kl3jvi.animity.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.utils.collectLatest
import com.kl3jvi.animity.utils.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var pagingController: PagingSearchController
    private var binding: FragmentSearchBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        pagingController = PagingSearchController(FirebaseAnalytics.getInstance(requireContext()))
        initViews()
        observeViewModel()
    }

    /**
     * It sets up the search view and recycler view.
     */
    private fun initViews() {
        binding?.apply {
            searchRecycler.setController(pagingController)
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    dismissKeyboard(binding?.mainSearch)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.onSearchQueryChanged(query)
                    return false
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }

    override fun onPause() {
        dismissKeyboard(binding?.mainSearch)
        super.onPause()
    }

    private fun observeViewModel() {
        collectLatest(viewModel.searchList) { animeData ->
            pagingController.submitData(animeData)
        }
    }
}
