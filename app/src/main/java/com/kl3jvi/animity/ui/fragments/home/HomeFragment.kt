package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
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
        collectFlow(viewModel.homeData) { result ->
            binding.mainRv.withModels {
                binding.loadingIndicator.isVisible = result.popularAnime.isEmpty()
                buildHome(result, firebaseAnalytics)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSettingsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) (activity as MainActivity?)?.showBottomNavBar()
    }

    private fun handleNetworkChanges() {
        requireActivity().isConnectedToInternet(viewLifecycleOwner) { isConnected ->
            /* Checking if the user is connected to the internet and if so, it will fetch the data from
            the server. */
            if (isConnected) fetchHomeData()
            binding.apply {
                mainRv.isVisible = isConnected
                loadingIndicator.isVisible = isConnected
                noInternetStatus.noInternet.isVisible = !isConnected
            }
        }
    }

    /**
     * It handles network changes.
     */
    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    override fun observeViewModel() {}

    override fun initViews() {
//        binding.setOnClickListener {
//            MediaListDialogFragment().show(parentFragmentManager, "dialog")
//        }
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)


}


