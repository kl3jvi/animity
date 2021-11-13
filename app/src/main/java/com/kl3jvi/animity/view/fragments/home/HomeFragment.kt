package com.kl3jvi.animity.view.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.view.activities.MainActivity
import com.kl3jvi.animity.view.adapters.CustomHorizontalAdapter
import com.kl3jvi.animity.view.adapters.CustomVerticalAdapter
import com.kl3jvi.animity.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var subAdapter: CustomHorizontalAdapter
    private lateinit var newSeasonAdapter: CustomHorizontalAdapter
    private lateinit var todayAdapter: CustomVerticalAdapter
    private lateinit var movieAdapter: CustomHorizontalAdapter
    private lateinit var snapHelperSub: SnapHelper
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apply {
            initViews()
            fetchRecentDub()
            getNewSeason()
            getMovies()
            getTodaySelectionAnime()
        }
    }

    private fun initViews() {
        // recent sub adapter
        binding.recentSub.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            subAdapter = CustomHorizontalAdapter(this@HomeFragment, arrayListOf())
            setHasFixedSize(true)
            snapHelperSub = PagerSnapHelper()
            snapHelperSub.attachToRecyclerView(this)
            adapter = subAdapter
        }

        // new season adapter
        binding.newSeasonRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            newSeasonAdapter = CustomHorizontalAdapter(this@HomeFragment, arrayListOf())
            setHasFixedSize(true)
            snapHelperSub = PagerSnapHelper()
            snapHelperSub.attachToRecyclerView(this)
            adapter = newSeasonAdapter
        }

        // movies adapter
        binding.moviesRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL, false
            )
            movieAdapter = CustomHorizontalAdapter(this@HomeFragment, arrayListOf())
            setHasFixedSize(true)
            snapHelperSub = PagerSnapHelper()
            snapHelperSub.attachToRecyclerView(this)
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
        viewModel.recentSubDub.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Success -> {
                    binding.recentSub.visibility = View.VISIBLE
                    binding.recentSubTv.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    res.data?.let { subAdapter.addAnimes(it) }
                }
                is Resource.Loading -> {
                    binding.recentSub.visibility = View.GONE
                    binding.recentSubTv.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
    }

    private fun getNewSeason() {
        viewModel.newSeason.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { animeList -> newSeasonAdapter.addAnimes(animeList) }
                    binding.newSeasonRv.visibility = View.VISIBLE
                    binding.newSeasonTv.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.newSeasonRv.visibility = View.GONE
                    binding.newSeasonTv.visibility = View.GONE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
    }

    private fun getMovies() {
        viewModel.movies.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { animeList -> movieAdapter.addAnimes(animeList) }
                    binding.moviesRv.visibility = View.VISIBLE
                    binding.moviesTv.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.moviesRv.visibility = View.GONE
                    binding.moviesTv.visibility = View.GONE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
    }

    private fun getTodaySelectionAnime() {
        viewModel.todaySelection.observe(viewLifecycleOwner, { res ->
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
                    showSnack(res.message)
                }
            }
        })
    }

    fun navigateToDetails(animeDetails: AnimeMetaModel) {
        try {
            findNavController().navigate(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnack(message: String?) {
        val snack = Snackbar.make(binding.root, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }
}
