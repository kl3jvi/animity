package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.Message
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient
) : MessageRepository {
    private val messageSentFlow = MutableSharedFlow<Unit>()

    override fun sendMessage(
        recipientId: Int,
        message: String,
        parentId: Int?
    ): Flow<Message> = flow {
        val recipientDetails = aniListGraphQlClient.getUserDataById(recipientId).convert()

        val sentMessage = aniListGraphQlClient.sendMessage(recipientId, message, parentId).convert(
            recipientId = recipientDetails.id,
            recipientName = recipientDetails.name,
            recipientAvatarLarge = recipientDetails.avatar.large,
            recipientAvatarMedium = recipientDetails.avatar.medium
        )

        // Emit a unit to trigger the flow to re-emit
        messageSentFlow.emit(Unit)
        emit(sentMessage)
    }

    override fun getMessages(userId: Int): Flow<List<Message>> = flow {
        val allMessages = aniListGraphQlClient.getMessages(userId)

        emit(
            allMessages.convert().filter { message ->
                message.recipient.id == userId || message.messenger.id == userId
            }
        )
    }

    override fun getNewMessagesFlow(
        userId: Int,
        pollingInterval: Long
    ): Flow<List<Message>> =
        messageSentFlow.onStart { emit(Unit) } // Emit a unit to trigger the flow to emit immediately
            .flatMapLatest { // Use flatMapLatest to switch to the latest emission
                flow {
                    emit(Unit) // Emit a unit to trigger the flow to emit immediately
                    while (true) {
                        delay(pollingInterval) // Add delay for polling interval
                        emit(Unit)
                    }
                }
            }.flatMapLatest { // Use flatMapLatest to switch to the latest emission
                getMessages(userId)
            }
}
