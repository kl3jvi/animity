package com.kl3jvi.animity.data.model.ui_models

data class NotificationData(
    val airingNotifications: List<Notification> = emptyList(),
    val followingNotifications: List<Notification> = emptyList(),
    val likeNotification: List<Notification> = emptyList(),
    val messageNotifications: List<Notification> = emptyList()
)
