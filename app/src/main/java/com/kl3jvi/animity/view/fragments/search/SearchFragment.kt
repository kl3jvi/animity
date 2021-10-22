package com.kl3jvi.animity.view.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.view.adapters.CustomSearchAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: CustomSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getSearchData()
        val sv = binding.mainSearch
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                viewModel.passQuery(query)
                return false
            }
        })

    }

    private fun initViews() {
        binding.searchRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            searchAdapter = CustomSearchAdapter(this@SearchFragment)
            adapter = searchAdapter
        }
    }

    private fun getSearchData() {
        viewModel.searchResult.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Success -> {
                    binding.searchLoadingBar.visibility = View.GONE
                    binding.noSearchResult.visibility = View.GONE
                    binding.searchRecycler.visibility = View.VISIBLE
                    res.data?.let { searchAdapter.passSearchData(it) }
                }
                is Resource.Loading -> {
                    binding.searchRecycler.visibility = View.GONE
                    binding.searchLoadingBar.visibility = View.VISIBLE
                    binding.noSearchResult.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
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
}