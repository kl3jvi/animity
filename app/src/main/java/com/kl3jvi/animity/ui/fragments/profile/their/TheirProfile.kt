package com.kl3jvi.animity.ui.fragments.profile.their

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentTheirProfileBinding
import com.kl3jvi.animity.ui.fragments.profile.my.buildProfile
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TheirProfile : Fragment(R.layout.fragment_their_profile) {

    private val viewModel: TheirProfileViewModel by viewModels()
    private var binding: FragmentTheirProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTheirProfileBinding.bind(view)
        getProfileData()
    }

    private fun getProfileData() {
        collect(viewModel.theirProfileData) { userData ->
            binding?.theirProfileRv?.withModels {
                when (userData) {
                    is UiResult.Error -> {
                        Toast.makeText(
                            requireContext(),
                            userData.throwable.localizedMessage ?: "",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    UiResult.Loading -> {}

                    is UiResult.Success -> {
                        buildProfile(userData = userData.data)
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
