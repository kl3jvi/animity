package com.kl3jvi.animity.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.offline.DownloadManager
import com.kl3jvi.animity.databinding.FragmentProfileBinding
import com.kl3jvi.animity.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    @Inject
    lateinit var downloadManager: DownloadManager
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

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)
}
