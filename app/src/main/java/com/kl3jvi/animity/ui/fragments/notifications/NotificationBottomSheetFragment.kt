package com.kl3jvi.animity.ui.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.data.enums.NotificationTitle
import com.kl3jvi.animity.databinding.NotificationsBottomSheetBinding
import com.kl3jvi.animity.notifications
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.Constants.Companion.randomId
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: NotificationsBottomSheetBinding? = null
    private val viewModel by viewModels<NotificationViewModel>()

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
        collect(viewModel.notifications) { notificationData ->
            Log.e("Notifications otraves", notificationData.toString())
            binding?.notificationsRv?.withModels {
                notificationData?.run {
                    listOf(
                        airingNotifications,
                        followingNotifications
                    )
                }?.forEachIndexed { index, notifications ->
                    title {
                        id(randomId())
                        title(NotificationTitle.values()[index].title)
                    }
                    notifications.forEach {
                        notifications {
                            id(it.id)
                            notification(it)
                        }
                    }
                }
            }
        }
    }
}
