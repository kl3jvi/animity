package com.kl3jvi.animity.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomSearchAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.ViewUtils.hide
import com.kl3jvi.animity.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {


    override val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: CustomSearchAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {}

    override fun initViews() {
        binding.apply {
            searchRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                searchAdapter = CustomSearchAdapter()
                adapter = searchAdapter
            }
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    search(query)
                    return false
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchAnimes(query).collectLatest { animeData ->
                binding.searchRecycler.show()
                searchAdapter.submitData(animeData)
                binding.noSearchResult.hide()
            }
        }
    }

}
