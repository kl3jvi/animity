package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.newAdapter.ParentAdapter
import com.kl3jvi.animity.ui.base.viewBinding
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.observeLiveData
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModels()
    val binding: FragmentHomeBinding by viewBinding()

    private val mainAdapter by lazy { ParentAdapter(playButtonFlag = false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initViews()
    }

    private fun observeViewModel() {
        fetchRecentDub()
    }

    private fun initViews() {
        // recent sub adapter
        val recyclerView = binding.mainRv
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
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


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    private fun handleNetworkChanges() {
        requireActivity().isConnectedToInternet().observe(viewLifecycleOwner) { isConnected ->
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
