package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import coil.load
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.databinding.FragmentProfileGuestBinding
import com.kl3jvi.animity.ui.activities.login.LoginActivity
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.Constants.Companion.DEFAULT_COVER
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.observeLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()
    private val guestBinding: FragmentProfileGuestBinding get() = guestView()
    override fun observeViewModel() {}
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

    private fun isGuestLogin(): Boolean {
        return (activity as MainActivity).isGuestLogin
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isGuestLogin()) {
            getProfileData()
            getAnimeListProfileData()
        } else {
            guestBinding.button2.cornerRadius = 10
        }
    }


    private fun getProfileData() {
        observeLiveData(viewModel.profileData, viewLifecycleOwner) {
            binding.bgImage.load(
                if (it.data?.user?.bannerImage.isNullOrEmpty())
                    DEFAULT_COVER
                else
                    it.data?.user?.bannerImage
            )
            binding.userData = it.data
        }
    }

    private fun getAnimeListProfileData() {
        observeLiveData(viewModel.animeList, viewLifecycleOwner) {
            binding.animeData = it.data
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


}
