package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.Message
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient
) : MessageRepository {
    private val messageSentFlow = MutableSharedFlow<Unit>()

    override suspend fun sendMessage(
        recipientId: Int,
        message: String,
        parentId: Int?
    ): Message {
        val recipientDetails = aniListGraphQlClient.getUserDataById(recipientId).convert()

        val sentMessage = aniListGraphQlClient.sendMessage(recipientId, message, parentId).convert(
            recipientId = recipientDetails.id,
            recipientName = recipientDetails.name,
            recipientAvatarLarge = recipientDetails.avatar.large,
            recipientAvatarMedium = recipientDetails.avatar.medium
        )

        // Emit a unit to trigger the flow to re-emit
        messageSentFlow.emit(Unit)

        return sentMessage
    }

    override suspend fun getMessages(userId: Int): List<Message> {
        val allMessages = aniListGraphQlClient.getMessages(userId)

        return allMessages.convert()
            .filter { message ->
                message.recipient.id == userId ||
                    message.messenger.id == userId
            }
    }

    override fun getNewMessagesFlow(
        userId: Int,
        pollingInterval: Long
    ): Flow<List<Message>> = messageSentFlow
        .onStart { emit(Unit) } // Emit a unit to trigger the flow to emit immediately
        .combine(flow { emit(Unit) }.onEach { delay(pollingInterval) }) { _, _ ->
            getMessages(userId)
        }
}
