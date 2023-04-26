package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.fragments.notifications.NotificationBottomSheetFragment
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.nav
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchHomeData()
        createFragmentMenu(R.menu.settings_menu) {
            when (it.itemId) {
                R.id.action_settings -> handleSettings()
                R.id.action_notifications -> handleNotifications()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        analytics.logCurrentScreen("Home")
    }

    private fun handleNotifications() {
        val bottomSheetFragment = NotificationBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun handleSettings() = findNavController().nav(
        R.id.navigation_home,
        HomeFragmentDirections.actionNavigationHomeToSettingsFragment()
    )

    private fun fetchHomeData() {
        collect(viewModel.homeDataUiState) { result ->
            binding?.mainRv?.withModels {
                when (result) {
                    is UiResult.Error -> showSnack(
                        binding?.root,
                        result.throwable.message ?: "Error occurred"
                    )

                    UiResult.Loading -> {}
                    is UiResult.Success -> {
                        Log.e(
                            "Media Status",
                            result.data.popularAnime.map { it.isFavourite }.toString()
                        )
                        buildHome(
                            result.data,
                            viewModel.analytics
                        )
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
}
