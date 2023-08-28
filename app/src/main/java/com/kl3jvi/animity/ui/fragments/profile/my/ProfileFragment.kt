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
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), StateManager {

    private val viewModel: ProfileViewModel by viewModels()
    private var binding: FragmentProfileBinding? = null

    @Inject
    lateinit var analytics: Analytics
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }

    private fun getProfileData() {
        collect(viewModel.profileData) { userData ->
            binding?.profileRv?.withModels {
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
//                        buildProfile(
//                            profileType = ProfileType.ME,
//                            userData = userData.data,
//                        ) {
//                            Log.e(empty {  },userData.data.followersAndFollowing)
//                        }
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

    override fun onResume() {
        super.onResume()
        analytics.logCurrentScreen("Profile")
    }

    override fun showLoading(show: Boolean) {
        binding?.loading?.isVisible = show
        binding?.profileRv?.isVisible = !show
    }

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
