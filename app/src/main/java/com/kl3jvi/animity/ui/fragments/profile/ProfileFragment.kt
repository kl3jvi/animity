package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.Constants.Companion.DEFAULT_COVER
import com.kl3jvi.animity.utils.collectFlow
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlow(viewModel.profileData) {
            binding.bgImage.load(
                if (it.data?.user?.bannerImage.isNullOrEmpty())
                    DEFAULT_COVER
                else
                    it.data?.user?.bannerImage
            )
            binding.userData = it.data
        }
    }

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)
}
