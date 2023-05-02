package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(userId: Int): List<Message>
    suspend fun sendMessage(recipientId: Int, message: String, parentId: Int?): Message
    fun getNewMessagesFlow(userId: Int, pollingInterval: Long = 5000L): Flow<List<Message>>
}
