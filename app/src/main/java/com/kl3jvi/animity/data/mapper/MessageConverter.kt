package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.GetMessagesQuery
import com.kl3jvi.animity.SendMessageMutation
import com.kl3jvi.animity.data.model.ui_models.Message
import com.kl3jvi.animity.utils.or1

fun ApolloResponse<SendMessageMutation.Data>.convert(
    recipientId: Int,
    recipientName: String,
    recipientAvatarLarge: String,
    recipientAvatarMedium: String
): Message {
    val sentReply = this.data?.sent
    val sentMessage = this.data?.sentMessage

    return if (sentReply != null) {
        val user = sentReply.user
        Message(
            id = sentReply.id,
            senderUserId = user?.id ?: throw Exception("Error sending message: user ID is null"),
            message = sentReply.text ?: throw Exception("Error sending message: message is null"),
            createdAt = sentReply.createdAt,
            recipient = Message.User(
                id = recipientId,
                name = recipientName,
                avatarLarge = recipientAvatarLarge,
                avatarMedium = recipientAvatarMedium
            ),
            messenger = Message.User(
                id = user?.id ?: throw Exception("Error sending message: user ID is null"),
                name = user.name,
                avatarLarge = user.avatar?.large ?: "",
                avatarMedium = user.avatar?.medium ?: ""
            ),
            parentId = null
        )
    } else if (sentMessage != null) {
        val recipient = sentMessage.recipient
        val messenger = sentMessage.messenger

        Message(
            id = sentMessage.id,
            senderUserId = messenger?.id
                ?: throw Exception("Error sending message: messenger ID is null"),
            message = sentMessage.message
                ?: throw Exception("Error sending message: message is null"),
            createdAt = sentMessage.createdAt,
            recipient = Message.User(
                id = recipient?.id
                    ?: throw Exception("Error sending message: recipient ID is null"),
                name = recipient.name,
                avatarLarge = recipient.avatar?.large ?: "",
                avatarMedium = recipient.avatar?.medium ?: ""
            ),
            messenger = Message.User(
                id = messenger.id,
                name = messenger.name,
                avatarLarge = messenger.avatar?.large ?: "",
                avatarMedium = messenger.avatar?.medium ?: ""
            ),
            parentId = null
        )
    } else {
        throw Exception("Error sending message: both sent and sentMessage are null")
    }
}

fun ApolloResponse<GetMessagesQuery.Data>.convert(): List<Message> {
    val messageActivities = this.data?.page?.activities ?: emptyList()

    return messageActivities.map { messageActivity ->
        val recipient = messageActivity?.onMessageActivity?.recipient
        val messenger = messageActivity?.onMessageActivity?.messenger

        val baseMessage = Message(
            id = messageActivity?.onMessageActivity?.id.or1(),
            senderUserId = messenger?.id.or1(),
            message = messageActivity?.onMessageActivity?.message ?: "",
            createdAt = messageActivity?.onMessageActivity?.createdAt.or1(),
            recipient = Message.User(
                id = recipient?.id ?: 0,
                name = recipient?.name ?: "",
                avatarLarge = recipient?.avatar?.large ?: "",
                avatarMedium = recipient?.avatar?.medium ?: ""
            ),
            messenger = Message.User(
                id = messenger?.id ?: 0,
                name = messenger?.name ?: "",
                avatarLarge = messenger?.avatar?.large ?: "",
                avatarMedium = messenger?.avatar?.medium ?: ""
            ),
            replies = emptyList()
        )
        baseMessage.copy(
            replies = messageActivity?.onMessageActivity?.replies?.mapNotNull { reply ->
                if (reply == null) {
                    null
                } else {
                    Message.Reply(
                        id = reply.id,
                        message = reply.text ?: "",
                        createdAt = reply.createdAt,
                        user = Message.User(
                            id = reply.user?.id ?: 0,
                            name = reply.user?.name ?: "",
                            avatarLarge = reply.user?.avatar?.large ?: "",
                            avatarMedium = reply.user?.avatar?.medium ?: ""
                        )
                    )
                }
            } ?: emptyList()
        )
    }
}
