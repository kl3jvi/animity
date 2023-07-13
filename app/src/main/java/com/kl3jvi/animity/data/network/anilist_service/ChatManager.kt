package com.kl3jvi.animity.data.network.anilist_service

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class ChatManager @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val TAG = "ChatManager"

    fun createChat(
        user1Id: String,
        user2Id: String
    ) {
        val sortedUserIds = listOf(user1Id, user2Id).sorted()
        val chatId = sortedUserIds.joinToString(separator = "")

        val chat = hashMapOf(
            "users" to sortedUserIds,
            "sender" to user1Id,
            "receipent" to user2Id
        )

        db.collection("chats").document(chatId).set(chat)
    }

    fun chatExists(user1Id: String, user2Id: String, callback: (Boolean) -> Unit) {
        val sortedUserIds = listOf(user1Id, user2Id).sorted()
        val chatId = sortedUserIds.joinToString(separator = "")

        db.collection("chats").document(chatId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(task.result.exists())
            } else {
                callback(false)
            }
        }
    }

    fun retrieveChatsForUser(userId: String, callback: (List<Chat>) -> Unit): Task<QuerySnapshot> {
        return db.collection("chats").whereArrayContains("users", userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chats = task.result.toObjects(Chat::class.java)
                    callback(chats)
                } else {
                    callback(emptyList())
                }
            }
    }

    fun sendMessage(chatId: String, senderId: String, messageContent: String) {
        val message = hashMapOf(
            "sender" to senderId,
            "timestamp" to FieldValue.serverTimestamp(),
            "content" to messageContent
        )

        db.collection("chats").document(chatId).collection("messages").add(message)
    }

    fun retrieveMessages(
        chatId: String,
        onNewMessages: (List<Message>) -> Unit
    ): ListenerRegistration {
        return db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { document ->
                        document.toObject(Message::class.java)
                    }
                    onNewMessages(messages)
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
    }

    data class Chat(
        val users: List<String>,
        val user1: Map<String, String>,
        val user2: Map<String, String>,
        val messages: List<Message>?
    )

    data class Message(
        val sender: String,
        val timestamp: Timestamp,
        val content: String
    )
}
