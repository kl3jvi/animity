package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewLifecycleOwner.collect(viewModel.homeDataUiState) { result ->
            binding?.mainRv?.withModels {
                when (result) {
                    is HomeDataUiState.Error -> showSnack(
                        binding?.root,
                        result.exception?.message ?: "Error occurred"
                    )
                    HomeDataUiState.Loading -> {}
                    is HomeDataUiState.Success -> {
                        buildHome(
                            result.data,
                            Firebase.analytics
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }
}
