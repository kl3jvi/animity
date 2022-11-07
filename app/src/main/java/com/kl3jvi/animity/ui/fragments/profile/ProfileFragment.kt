package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding
        .inflate(inflater)
        .also { binding = it }
        .run { root }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragmentMenu(menuLayout = R.menu.profile_menu) {
            when (it.itemId) {
                R.id.action_log_out -> {
                    viewModel.clearStorage() // Deletes saved token
                    requireActivity().launchActivity<LoginActivity> { }
                    requireActivity().finish()
                    true
                }

                else -> false
            }

        }
        getProfileData()
    }

    private fun getProfileData() {
        collectFlow(viewModel.profileData) { userData ->
            when (userData) {
                is ProfileDataUiState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "${userData.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                ProfileDataUiState.Loading -> {}

                is ProfileDataUiState.Success -> {
                    binding.profileRv.withModels { buildProfile(userData = userData.data) }
                }
            }
        }
    }
}
