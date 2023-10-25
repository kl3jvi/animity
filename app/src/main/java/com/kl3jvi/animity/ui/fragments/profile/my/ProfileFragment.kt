package com.kl3jvi.animity.ui.fragments.profile.my

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.utils.BottomNavScrollListener
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.epoxy.setupBottomNavScrollListener
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModels()
    private var binding: FragmentProfileBinding? = null
    private lateinit var listener: BottomNavScrollListener

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        createFragmentMenu(menuLayout = R.menu.profile_menu) {
            when (it.itemId) {
                R.id.action_log_out -> {
                    viewModel.clearStorage {
                        requireActivity().launchActivity<LoginActivity> {
                            binding = null
                        }
                        requireActivity().finish()
                    } // Deletes saved token
                }
            }
        }
        getProfileData()
        listener = requireActivity() as BottomNavScrollListener
    }

    private fun getProfileData() {
        collect(viewModel.profileData) { userData ->
            binding?.profileRv
                ?.setupBottomNavScrollListener(listener)
                ?.withModels {
                    when (userData) {
                        is UiResult.Error -> {
                            Toast.makeText(
                                requireContext(),
                                userData.throwable.localizedMessage ?: "",
                                Toast.LENGTH_LONG,
                            ).show()
                            showLoading(false)
                        }

                        UiResult.Loading -> showLoading(true)

                        is UiResult.Success -> {
                            showLoading(false)
                            buildProfile(
                                profileType = ProfileType.ME,
                                userData = userData.data,
                            )
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        analytics.logCurrentScreen("Profile")
    }

     fun showLoading(show: Boolean) {
        binding?.loading?.isVisible = show
        binding?.profileRv?.isVisible = !show
    }

     fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
