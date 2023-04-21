package com.kl3jvi.animity.ui.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.notifications
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by viewModels<NotificationViewModel>()
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
        pagingController = NotificationsController()
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.notificationsRv?.setController(pagingController)
        collect(viewModel.notifications) { notificationData ->
            pagingController.submitData(notificationData)
            Log.e("Notifications otraves", notificationData.toString())
//            binding?.notificationsRv?.withModels {
//
//
//                notificationData?.run {
//                    listOf(
//                        airingNotifications,
//                        followingNotifications,
//                        likeNotification
//                    )
//                }?.forEachIndexed { index, notifications ->
//                    title {
//                        id(randomId())
//                        title(NotificationTitle.values()[index].title)
//                    }
//                    notifications.forEach {
//                        notifications {
//                            id(it.id)
//                            notification(it)
//                        }
//                    }
//                }
//            }
        }
    }
}