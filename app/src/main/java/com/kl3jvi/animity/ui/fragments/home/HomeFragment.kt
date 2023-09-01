package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.ui.fragments.notifications.NotificationBottomSheetFragment
import com.kl3jvi.animity.ui.fragments.schedule.ScheduleViewModel
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
class HomeFragment : Fragment(R.layout.fragment_home), StateManager {
    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null
    private val scheduleViewModel by activityViewModels<ScheduleViewModel>()

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchHomeData()
        scheduleViewModel()
        createFragmentMenu(R.menu.settings_menu) {
            when (it.itemId) {
                R.id.action_settings -> handleSettings()
                R.id.action_notifications -> handleNotifications()
                R.id.action_airing_schedule -> handleAiringScheduler()
            }
        }
    }

    private fun handleAiringScheduler() = findNavController().nav(
        R.id.navigation_home,
        HomeFragmentDirections.toSchedule(),
    )

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
        HomeFragmentDirections.toSettings(),
    )

    private fun fetchHomeData() {
        collect(viewModel.homeDataUiState) { result ->
            when (result) {
                is UiResult.Error -> handleError(result.throwable)
                UiResult.Loading -> showLoading(true)
                is UiResult.Success -> {
                    showLoading(false)
                    binding?.mainRv?.withModels {
                        buildHome(
                            result.data,
                            viewModel.analytics,
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

    override fun showLoading(show: Boolean) = with(binding) {
        this?.mainRv?.isVisible = !show
        this?.loading?.isVisible = show
    }

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
