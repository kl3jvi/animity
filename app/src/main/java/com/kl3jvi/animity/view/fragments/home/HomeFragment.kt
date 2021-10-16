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
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder
import com.kl3jvi.animity.utils.Status
import com.kl3jvi.animity.view.activities.MainActivity
import com.kl3jvi.animity.view.adapters.CustomHorizontalAdapter
import com.kl3jvi.animity.view.adapters.CustomVerticalAdapter
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputEditText


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
    }
    private lateinit var subAdapter: CustomHorizontalAdapter
    private lateinit var newSeasonAdapter: CustomHorizontalAdapter
    private lateinit var todayAdapter: CustomVerticalAdapter
    private lateinit var movieAdapter: CustomHorizontalAdapter
    private val snapHelperSub: SnapHelper = PagerSnapHelper()
    private val snapHelperNewSeason: SnapHelper = PagerSnapHelper()
    private val snapHelperMovies: SnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        getNewSeason()
        fetchRecentDub()
        getTodaySelectionAnime()
        fetchMovies()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // recent sub adapter
        binding.recentSub.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        subAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.recentSub.setHasFixedSize(true)
        snapHelperSub.attachToRecyclerView(binding.recentSub)
        binding.recentSub.adapter = subAdapter

        // new season adapter
        binding.newSeasonRv.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        newSeasonAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.newSeasonRv.setHasFixedSize(true)
        snapHelperNewSeason.attachToRecyclerView(binding.newSeasonRv)
        binding.newSeasonRv.adapter = newSeasonAdapter

        // movies adapter
        binding.moviesRv.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        movieAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.moviesRv.setHasFixedSize(true)
        snapHelperMovies.attachToRecyclerView(binding.moviesRv)
        binding.moviesRv.adapter = movieAdapter


        // today selection adapter
        binding.todaySelection.layoutManager = LinearLayoutManager(requireContext())
        binding.todaySelection.setHasFixedSize(true)
        todayAdapter = CustomVerticalAdapter(this)
        binding.todaySelection.adapter = todayAdapter


    }


    private fun fetchRecentDub() {

        viewModel.fetchRecentSubOrDub().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recentSub.visibility = View.VISIBLE
                        binding.recentSubTv.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { entry -> retrieveAnimes(entry) }
                    }
                    Status.ERROR -> {
                        showSnack(res.message)
                    }
                    Status.LOADING -> {
                        binding.recentSub.visibility = View.GONE
                        binding.recentSubTv.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun getTodaySelectionAnime() { // today selection anime
        viewModel.fetchTodaySelectionAnime().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { entry -> todayAdapter.getSelectedAnime(entry) }
                        binding.todaySelection.visibility = View.VISIBLE
                        binding.todSelectionTv.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        showSnack(res.message)
                    }
                    Status.LOADING -> {
                        binding.todaySelection.visibility = View.GONE
                        binding.todSelectionTv.visibility = View.GONE
                    }
                }
            }

        })
    }


    private fun getNewSeason() {
        viewModel.fetchNewSeason().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { entry ->
                            newSeasonAdapter.addAnimes(entry)
                        }
                        binding.newSeasonRv.visibility = View.VISIBLE
                        binding.newSeasonTv.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        showSnack(res.message)
                    }
                    Status.LOADING -> {
                        binding.newSeasonRv.visibility = View.GONE
                        binding.newSeasonTv.visibility = View.GONE
                    }
                }
            }

        })
    }

    private fun fetchMovies() {
        viewModel.fetchMovies().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { entry ->
                            movieAdapter.addAnimes(entry)
                        }
                        binding.moviesRv.visibility = View.VISIBLE
                        binding.moviesTv.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        showSnack(res.message)
                    }
                    Status.LOADING -> {
                        binding.moviesRv.visibility = View.GONE
                        binding.moviesTv.visibility = View.GONE
                    }
                }
            }

        })
    }


    fun animeDetails(animeDetails: AnimeMetaModel) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                animeDetails
            )
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun retrieveAnimes(animes: List<AnimeMetaModel>) {
        subAdapter.apply {
            addAnimes(animes = animes)
            notifyDataSetChanged()
        }
    }
}