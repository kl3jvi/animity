package com.kl3jvi.animity.ui.fragments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.MessageRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    savedStateHandle: SavedStateHandle,
    localStorage: PersistenceRepository
) : ViewModel() {
    private var parentId: Int? = null
    val myUserId = localStorage.aniListUserId?.toInt().or1()

    val messageList = savedStateHandle.getStateFlow("userId", -1)
        .flatMapLatest { userId ->
            messageRepository.getNewMessagesFlow(userId)
        }.map { messageList ->
            parentId = messageList.first().parentId
            messageList.sortedBy { message ->
                message.createdAt
            }
        }.asResult()
        .distinctUntilChanged()

    fun sendMessage(recipientId: Int, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.sendMessage(recipientId, message, parentId)
                .asResult()
                .collect()
        }
    }
}
