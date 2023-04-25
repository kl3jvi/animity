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
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.utils.collectLatest
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by activityViewModels<NotificationViewModel>()
    private lateinit var pagingController: NotificationsController

    @Inject
    lateinit var analytics: Analytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationsBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
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
        pagingController = NotificationsController { item ->
            val directions = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                item?.media ?: AniListMedia(),
                item?.episode.or1()
            )
            findNavController().navigate(directions)
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
        val viewsToUpdate = listOf(
            binding?.progress,
            binding?.noNotifications
        )

        for (view in viewsToUpdate) {
            view?.isVisible = list.isEmpty()
        }

        binding?.progress?.isVisible = binding?.notificationsRv?.isVisible?.not() == false
    }
}
