package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.createFragmentMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding
        .inflate(inflater)
        .also { binding = it }
        .run { root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragmentMenu(R.menu.settings_menu) { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSettingsFragment()); true
                }

                else -> false
            }
        }
        handleNetworkChanges()
    }

    private fun fetchHomeData() {
        viewLifecycleOwner.collectFlow(viewModel.homeDataUiState) { result ->
            binding.mainRv.withModels {
                when (result) {
                    is HomeDataUiState.Error -> showSnack(
                        binding.root,
                        result.exception?.message ?: "Error occurred"
                    )

                    HomeDataUiState.Loading -> {
                        binding.loadingIndicator.isVisible = true
                    }

                    is HomeDataUiState.Success -> {
                        binding.loadingIndicator.isVisible = false
                        buildHome(
                            result.data,
                            Firebase.analytics
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) (activity as MainActivity?)?.showBottomNavBar()
    }

    private fun handleNetworkChanges() {
        collectFlow(viewModel.connection) { isConnected ->
            Log.e("Internet", isConnected.toString())

            if (isConnected) fetchHomeData()
            binding.apply {
                mainRv.isVisible = isConnected
                noInternetStatus.noInternet.isVisible = !isConnected
            }
        }
    }
}
