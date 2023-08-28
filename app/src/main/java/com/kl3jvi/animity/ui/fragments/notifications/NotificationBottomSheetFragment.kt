package com.kl3jvi.animity.ui.fragments.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.NotificationType
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectLatest
import com.kl3jvi.animity.utils.epoxy.NotificationsController
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment(), StateManager {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by activityViewModels<NotificationViewModel>()
    private lateinit var pagingController: NotificationsController

    @Inject
    lateinit var analytics: Analytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = NotificationsBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false,
        )
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNotificationsList()
        initializeToolbarDismissAction()
    }

    private fun initializeToolbarDismissAction() {
        binding?.notificationToolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.quit_notifications -> {
                    dismiss() // Dismiss the bottom sheet
                    true
                }

                else -> false
            }
        }
    }

    private fun setupNotificationsList() {
        pagingController = NotificationsController { item, type ->

            when (type) {
                NotificationType.Airing -> {
                    val directions = HomeFragmentDirections.toDetails(
                        item?.media ?: AniListMedia(),
                        item?.episode.or1(),
                    )
                    findNavController().navigate(directions)
                }

                is NotificationType.Activity -> {
                    val directions = HomeFragmentDirections.toTheirProfile(type.user)
                    findNavController().navigate(directions)
                }

                is NotificationType.Following -> {
                    val directions = HomeFragmentDirections.toTheirProfile(type.user)
                    findNavController().navigate(directions)
                }

                NotificationType.Threads -> {}
                NotificationType.Unknown -> {}
            }
        }

        binding?.notificationsRv?.setController(pagingController)
        binding?.notificationsRv?.layoutManager = LinearLayoutManager(requireContext())
        collectLatest(viewModel.notifications) { notificationData ->
            pagingController.submitData(notificationData)
        }
        pagingController.addInterceptor(::updateViewVisibility)
    }

    override fun onResume() {
        super.onResume()
        analytics.logCurrentScreen("Notifications")
    }

    private fun updateViewVisibility(list: List<Any>) {
        val show = list.isEmpty()
        showLoading(show)
    }

    override fun showLoading(show: Boolean) {
        binding?.progress?.isVisible = show
        binding?.notificationsRv?.isVisible = !show
    }

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
