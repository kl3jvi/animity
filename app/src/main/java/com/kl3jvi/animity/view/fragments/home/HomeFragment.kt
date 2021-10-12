package com.kl3jvi.animity.view.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        getPopularAnime()
        fetchMovies()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recent subb adapter
        binding.recentSub.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        subAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.recentSub.adapter = subAdapter

        // today selection adapter
        binding.todaySelection.layoutManager = LinearLayoutManager(requireContext())
        todayAdapter = CustomVerticalAdapter(this)
        binding.todaySelection.adapter = todayAdapter

        // new season adapter
        binding.newSeasonRv.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        newSeasonAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.newSeasonRv.adapter = newSeasonAdapter

        // movies adapter
        binding.moviesRv.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        movieAdapter = CustomHorizontalAdapter(this, arrayListOf())
        binding.moviesRv.adapter = movieAdapter


    }


    private fun fetchRecentDub() {
        viewModel.fetchRecentSubOrDub().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->

                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recentSub.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { entry -> retrieveAnimes(entry) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.recentSub.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun getPopularAnime() {
        viewModel.fetchPopularAnime().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { entry ->
                            todayAdapter.getSelectedAnime(entry)
                        }
                        binding.recentSub.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.recentSub.visibility = View.GONE
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
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG).show()
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
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG).show()
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
            R.id.more_menu -> {
                InputSheet().show(requireContext()) {
                    with(InputEditText {
                        hint("Search Animes")
                        drawable(R.drawable.ic_search)
                        changeListener { value ->
                            println(value)
                        } // Input value changed
                        resultListener { value ->
                            println(value)
                        } // Input value changed when form finished
                    })
                }

            }
        }
        return super.onOptionsItemSelected(item)
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