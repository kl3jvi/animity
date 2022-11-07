package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
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
        fetchHomeData()
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

                    }

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

    override fun onResume() {
        super.onResume()
    }
}
