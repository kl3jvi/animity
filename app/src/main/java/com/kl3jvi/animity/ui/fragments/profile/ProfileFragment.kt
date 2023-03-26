package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private var binding: FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        createFragmentMenu(menuLayout = R.menu.profile_menu) {
            when (it.itemId) {
                R.id.action_log_out -> {
                    viewModel.clearStorage() // Deletes saved token
                    requireActivity().launchActivity<LoginActivity> {
                        binding = null
                    }
                    requireActivity().finish()
                    true
                }

                else -> false
            }
        }
        getProfileData()
    }

    private fun getProfileData() {
        viewLifecycleOwner.collect(viewModel.profileData) { userData ->
            binding?.profileRv?.withModels {
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
