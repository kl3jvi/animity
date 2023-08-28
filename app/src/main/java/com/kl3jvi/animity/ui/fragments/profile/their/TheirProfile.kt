package com.kl3jvi.animity.ui.fragments.profile.their

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentTheirProfileBinding
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.ui.fragments.profile.my.ProfileType
import com.kl3jvi.animity.ui.fragments.profile.my.buildProfile
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TheirProfile : Fragment(R.layout.fragment_their_profile), StateManager {

    private val viewModel: TheirProfileViewModel by viewModels()
    private var binding: FragmentTheirProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTheirProfileBinding.bind(view)
        getProfileData()
        toggleFollowFunctionality()
    }

    private fun toggleFollowFunctionality() {
    }

    private fun getProfileData() {
        collect(viewModel.theirProfileData) { userData ->
            binding?.theirProfileRv?.withModels {
                when (userData) {
                    is UiResult.Error -> {
                        Toast.makeText(
                            requireContext(),
                            userData.throwable.localizedMessage ?: "",
                            Toast.LENGTH_LONG,
                        ).show()
                        findNavController().popBackStack()
                    }

                    UiResult.Loading -> showLoading(true)

                    is UiResult.Success -> {
                        showLoading(false)
                        buildProfile(
                            userData = userData.data,
                            profileType = ProfileType.OTHER,
                            listener = {
                                viewModel.followUser()
                            },
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

    override fun showLoading(show: Boolean) {
        binding?.loading?.isVisible = show
        binding?.theirProfileRv?.isVisible = !show
    }

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
