package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.preload.EpoxyModelPreloader
import com.airbnb.epoxy.preload.PreloadRequestHolder
import com.airbnb.epoxy.preload.ViewMetadata
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.title
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.observeLiveData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun fetchHomeData() {
        observeLiveData(viewModel.homeData, viewLifecycleOwner) { result ->
            binding.mainRv.withModels {
                binding.loadingIndicator.isVisible = result.newAnime.isEmpty()
                buildHome(result, firebaseAnalytics)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) (activity as MainActivity?)?.showBottomNavBar()
    }

    private fun handleNetworkChanges() {
        requireActivity().isConnectedToInternet(viewLifecycleOwner) { isConnected ->
            if (isConnected) fetchHomeData()

            binding.apply {
                mainRv.isVisible = isConnected
                loadingIndicator.isVisible = isConnected
                noInternetStatus.noInternet.isVisible = !isConnected
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    override fun observeViewModel() {}


    override fun initViews() {
//        binding.button2.setOnClickListener {
//            requireContext().launchActivity<PaymentActivity> {
//                val dropInRequest = DropInRequest()
//                dropInClient = DropInClient(
//                    requireContext(),
//                    "sandbox_ykvmgk4j_fssw4nqtc2phhht8",
//                    dropInRequest
//                )
//                dropInClient.launchDropInForResult(
//                    requireActivity(),
//                    PaymentActivity.DROP_IN_REQUEST_CODE
//                )
//            }
//        }
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    companion object {
        const val DROP_IN_REQUEST_CODE = 800
    }
}


