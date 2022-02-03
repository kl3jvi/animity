package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomHorizontalAdapter
import com.kl3jvi.animity.ui.adapters.CustomVerticalAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.NetworkUtils
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.ViewUtils.hide
import com.kl3jvi.animity.utils.ViewUtils.show
import com.kl3jvi.animity.utils.navigateToDestination
import com.kl3jvi.animity.utils.observeLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var subAdapter: CustomHorizontalAdapter
    private lateinit var newSeasonAdapter: CustomHorizontalAdapter
    private lateinit var todayAdapter: CustomVerticalAdapter
    private lateinit var movieAdapter: CustomHorizontalAdapter
    private lateinit var snapHelper: SnapHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {
        fetchRecentDub()
        getNewSeason()
        getMovies()
        getTodaySelectionAnime()
    }

    override fun initViews() {
        // recent sub adapter
        binding.recentSub.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            subAdapter = CustomHorizontalAdapter()
            setHasFixedSize(true)
            snapHelper = PagerSnapHelper()
            if (this.onFlingListener == null)
                snapHelper.attachToRecyclerView(this);
            adapter = subAdapter
        }

        // new season adapter
        binding.newSeasonRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            newSeasonAdapter = CustomHorizontalAdapter()
            setHasFixedSize(true)
            snapHelper = PagerSnapHelper()
            if (this.onFlingListener == null)
                snapHelper.attachToRecyclerView(this);
            adapter = newSeasonAdapter
        }

        // movies adapter
        binding.moviesRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            movieAdapter = CustomHorizontalAdapter()
            setHasFixedSize(true)
            snapHelper = PagerSnapHelper()
            if (this.onFlingListener == null)
                snapHelper.attachToRecyclerView(this);
            adapter = movieAdapter
        }

        // today selection adapter
        binding.todaySelection.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            todayAdapter = CustomVerticalAdapter(this@HomeFragment, arrayListOf())
            adapter = todayAdapter
        }
    }

    private fun fetchRecentDub() {
        observeLiveData(viewModel.recentSubDub, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    binding.recentSub.visibility = View.VISIBLE
                    binding.recentSubTv.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    res.data?.let { subAdapter.submitList(it) }
                }
                is Resource.Loading -> {
                    binding.recentSub.visibility = View.GONE
                    binding.recentSubTv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    showSnack(binding.root, res.message)
                }
            }

        }
    }

    private fun getNewSeason() {
        observeLiveData(viewModel.newSeason, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { animeList -> newSeasonAdapter.submitList(animeList) }
                    binding.newSeasonRv.visibility = View.VISIBLE
                    binding.newSeasonTv.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.newSeasonRv.visibility = View.GONE
                    binding.newSeasonTv.visibility = View.GONE
                }
                is Resource.Error -> {
                    showSnack(binding.root, res.message)
                }
            }

        }
    }

    private fun getMovies() {
        observeLiveData(viewModel.movies, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { animeList -> movieAdapter.submitList(animeList) }
                    binding.moviesRv.visibility = View.VISIBLE
                    binding.moviesTv.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.moviesRv.visibility = View.GONE
                    binding.moviesTv.visibility = View.GONE
                }
                is Resource.Error -> {
                    showSnack(binding.root, res.message)
                }
            }

        }
    }

    private fun getTodaySelectionAnime() {
        observeLiveData(viewModel.todaySelection, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { entry -> todayAdapter.getSelectedAnime(entry) }
                    binding.todaySelection.visibility = View.VISIBLE
                    binding.todSelectionTv.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.todaySelection.visibility = View.GONE
                    binding.todSelectionTv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    showSnack(binding.root, res.message)
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
            if (!isGuestLogin() && isConnected) {
                binding.apply {
                    mainScroll.show()
                    noInternet.hide()
                }
            } else {
                binding.apply {
                    noInternet.show()
                    mainScroll.hide()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    private fun isGuestLogin(): Boolean {
        return (activity as MainActivity).isGuestLogin
    }
}
