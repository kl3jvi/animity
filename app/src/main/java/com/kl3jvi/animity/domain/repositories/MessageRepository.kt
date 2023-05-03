package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(userId: Int): Flow<List<Message>>
    fun sendMessage(recipientId: Int, message: String, parentId: Int?): Flow<Message>
    fun getNewMessagesFlow(userId: Int, pollingInterval: Long = 5000L): Flow<List<Message>>
}
