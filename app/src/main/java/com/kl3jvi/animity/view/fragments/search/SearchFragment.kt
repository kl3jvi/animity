package com.kl3jvi.animity.view.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.animity.databinding.FragmentSearchBinding
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
        viewModel.searchResult.observe(viewLifecycleOwner, {
            searchAdapter.passSearchData(it.data?: listOf())
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}