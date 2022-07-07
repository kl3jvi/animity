package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()
    override fun observeViewModel() {}
    override fun initViews() {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    private fun getProfileData() {
        collectFlow(viewModel.profileData) { userData ->
            collectFlow(viewModel.animeList) { animeCollectionResponse ->
                animeCollectionResponse?.let {
                    val hasNoData = animeCollectionResponse.isEmpty()
                    binding.progressBar.isVisible = hasNoData
                    binding.profileRv.isVisible = !hasNoData
                    binding.profileRv.withModels { buildProfile(userData, animeCollectionResponse) }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragmentMenu(menuLayout = R.menu.profile_menu) {
            when (it.itemId) {
                R.id.action_log_out -> {
                    viewModel.clearStorage()
                    requireActivity().launchActivity<LoginActivity> { }
                    requireActivity().finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    private fun handleNetworkChanges() {
        requireActivity().isConnectedToInternet(viewLifecycleOwner) { isConnected ->
            if (isConnected) getProfileData()
            binding.noInternetResult.noInternet.isVisible = !isConnected
            binding.profileRv.isVisible = isConnected
        }
    }


}

