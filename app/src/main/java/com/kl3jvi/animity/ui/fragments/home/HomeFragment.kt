package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomVerticalAdapter
import com.kl3jvi.animity.ui.adapters.newAdapter.ParentAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    //    private val subAdapter by lazy { CustomHorizontalAdapter() }
//    private val newSeasonAdapter by lazy { CustomHorizontalAdapter() }
    private val todayAdapter by lazy { CustomVerticalAdapter(this@HomeFragment, arrayListOf()) }

    //    private val movieAdapter by lazy { CustomHorizontalAdapter() }
    private val snapHelper by lazy { PagerSnapHelper() }
    private val mainAdapter by lazy { ParentAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {
        fetchRecentDub()
    }

    override fun initViews() {
        // recent sub adapter
        val recyclerView = binding.mainRv

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            adapter = mainAdapter
        }

    }

    private fun fetchRecentDub() {
        observeLiveData(viewModel.homeData, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Error -> {
                    binding.mainRv.hide()
                }
                is Resource.Loading -> {
                    binding.mainRv.hide()
                    binding.loadingIndicator.show()
                }
                is Resource.Success -> {
                    binding.loadingIndicator.hide()
                    binding.mainRv.show()
                    mainAdapter.submitList(res.data)
                }
            }
        }
    }


    fun navigateToDetails(animeDetails: AnimeMetaModel) {
        try {
            navigateToDestination<HomeFragmentDirections>(
                HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                    animeDetails
                )
            )
            if (requireActivity() is MainActivity) {
                (activity as MainActivity?)?.hideBottomNavBar()
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(this) { isConnected ->
            if (isConnected) {
                binding.apply {
                    mainRv.show()
                    noInternet.hide()
                }
            } else {
                binding.apply {
                    noInternet.show()
                    mainRv.hide()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }


}
