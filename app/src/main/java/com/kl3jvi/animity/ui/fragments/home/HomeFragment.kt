package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.viewBinding
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.observeLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModels()
    val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchHomeData()
    }

    private fun fetchHomeData() {
        observeLiveData(viewModel.homeData, viewLifecycleOwner) { result ->
            binding.mainRv.withModels {
                binding.loadingIndicator.isVisible = result.newAnime.isEmpty()
                buildHome(result)
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
        requireActivity().isConnectedToInternet(viewLifecycleOwner) { isConnected ->
            binding.apply {
                mainRv.isVisible = isConnected
                noInternetStatus.noInternet.isVisible = !isConnected
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }
}


