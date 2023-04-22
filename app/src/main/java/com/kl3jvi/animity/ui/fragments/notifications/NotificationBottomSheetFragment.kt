package com.kl3jvi.animity.ui.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.utils.collectLatest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by activityViewModels<NotificationViewModel>()
    private lateinit var pagingController: NotificationsController

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
        pagingController = NotificationsController()
        binding?.notificationsRv?.setController(pagingController)
        binding?.notificationsRv?.layoutManager = LinearLayoutManager(requireContext())
        collectLatest(viewModel.notifications) { notificationData ->
            Log.e("NOTIFIACTIONs", notificationData.toString())
            pagingController.submitData(notificationData)
        }

        pagingController.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                binding?.progress?.isVisible =
                    (binding?.notificationsRv?.adapter?.itemCount ?: 0) == 0
            }
        }
    }
}
