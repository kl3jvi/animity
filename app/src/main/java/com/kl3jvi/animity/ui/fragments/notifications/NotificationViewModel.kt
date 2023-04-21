package com.kl3jvi.animity.ui.fragments.notifications

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.domain.repositories.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    notificationsRepository: NotificationsRepository
) : ViewModel() {
    val notifications = notificationsRepository.getNotifications()
}
