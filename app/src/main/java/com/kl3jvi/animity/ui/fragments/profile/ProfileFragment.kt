package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.databinding.FragmentProfileGuestBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.observeLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()
    private val guestBinding: FragmentProfileGuestBinding get() = guestView()

    override fun observeViewModel() {
        if (!isGuestLogin()) {
            getProfileData()
        } else guestViewSignIn()
    }

    override fun initViews() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isGuestLogin()) setHasOptionsMenu(true)
        else setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (!isGuestLogin()) binding.root else guestBinding.root
    }


    private fun guestViewSignIn() {
        guestBinding.signInBack.setOnClickListener {
            with(requireContext()) {
                launchActivity<LoginActivity> {}
            }
        }
    }

    private fun getProfileData() {
        observeLiveData(viewModel.profileData, viewLifecycleOwner) { userData ->
            observeLiveData(viewModel.animeList, viewLifecycleOwner) { animeCollectionResponse ->
                binding.profileRv.withModels { buildProfile(userData, animeCollectionResponse) }
            }
        }
    }

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    private fun guestView(): FragmentProfileGuestBinding =
        FragmentProfileGuestBinding.inflate(layoutInflater)


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_log_out -> {
                viewModel.clearStorage()
                requireActivity().launchActivity<LoginActivity> { }
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
            binding.noInternetResult.noInternet.isVisible = !isConnected
            binding.profileRv.isVisible = isConnected
        }
    }


}
