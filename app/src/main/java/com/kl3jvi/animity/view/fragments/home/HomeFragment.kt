package com.kl3jvi.animity.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder
import com.kl3jvi.animity.utils.Status
import com.kl3jvi.animity.view.adapters.CustomSubAdapter

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
    }
    private lateinit var adapter: CustomSubAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recentSub.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL, false
        )
        adapter = CustomSubAdapter(this)
        binding.recentSub.adapter = adapter
        viewModel.fetchRecentSubOrDub().observe(viewLifecycleOwner, { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { entry ->
                            adapter.getAnimes(entry)
                        }
                        binding.recentSub.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recentSub.visibility = View.GONE
                    }
                }
            }

        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}