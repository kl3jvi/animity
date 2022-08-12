package com.kl3jvi.animity.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.collectLatestFlow
import com.kl3jvi.animity.utils.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var pagingController: PagingSearchController

    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pagingController = PagingSearchController(firebaseAnalytics)
        return binding.root
    }

    /**
     * It sets up the search view and recycler view.
     */
    override fun initViews() {
        binding.apply {
            searchRecycler.setController(pagingController)
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    dismissKeyboard(binding.mainSearch)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.onSearchQueryChanged(query)
                    return false
                }
            })
        }

    }

    override fun onPause() {
        dismissKeyboard(binding.mainSearch)
        super.onPause()
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }


    override fun observeViewModel() {
        collectLatestFlow(viewModel.searchList) { animeData ->
            pagingController.submitData(animeData)
        }
    }

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)
}

