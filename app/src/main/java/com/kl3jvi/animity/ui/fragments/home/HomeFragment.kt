package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentHomeBinding
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.ui.fragments.notifications.NotificationBottomSheetFragment
import com.kl3jvi.animity.ui.fragments.schedule.ScheduleViewModel
import com.kl3jvi.animity.utils.BottomNavScrollListener
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.createFragmentMenu
import com.kl3jvi.animity.utils.epoxy.setupBottomNavScrollListener
import com.kl3jvi.animity.utils.handleUiState
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

    private val listener: BottomNavScrollListener by lazy {
        requireActivity() as BottomNavScrollListener
    }

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fetchHomeData()
        scheduleViewModel()
        createFragmentMenu(R.menu.settings_menu) {
            when (it.itemId) {
                R.id.action_settings -> handleSettings()
                R.id.action_notifications -> handleNotifications()
                R.id.action_airing_schedule -> handleAiringScheduler()
                else -> Unit
            }
        }
    }

    private fun handleAiringScheduler() =
        nav(
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

    private fun handleSettings() =
        nav(
            R.id.navigation_home,
            HomeFragmentDirections.toSettings(),
        )

    private fun fetchHomeData() {
        collect(viewModel.homeDataUiState) { result ->
            handleUiState(
                uiState = result,
                contentView = binding?.mainRv!!,
                loadingView = binding?.loading!!,
                errorAction = ::handleError,
                contentAction = {
                    binding?.mainRv
                        ?.setupBottomNavScrollListener(listener)
                        ?.withModels {
                            buildHome(
                                it,
                                viewModel.analytics,
                            )
                        }
                },
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun showLoading(show: Boolean) =
        with(binding) {
            this?.mainRv?.isVisible = !show
            this?.loading?.isVisible = show
        }

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
