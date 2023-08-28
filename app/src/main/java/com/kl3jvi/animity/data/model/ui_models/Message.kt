package com.kl3jvi.animity.data.model.ui_models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val id: Int,
    val senderUserId: Int,
    val message: String,
    val createdAt: Int,
    val recipient: User,
    val messenger: User,
    val parentId: Int? = null, // Add parentId field
    val replies: List<Reply> = listOf(),
) {
    fun convertUnixTimeToFormattedTime(): String {
        val date = Date(createdAt * 1000L)
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

    data class User(
        val id: Int,
        val name: String,
        val avatarLarge: String,
        val avatarMedium: String,
    )

    data class Reply(
        val id: Int,
        val message: String,
        val createdAt: Int,
        val user: User,
    )
}
