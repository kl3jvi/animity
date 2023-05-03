package com.kl3jvi.animity.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.Message
import com.kl3jvi.animity.databinding.FragmentMessageBinding
import com.kl3jvi.animity.recipientMessage
import com.kl3jvi.animity.senderMessage
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.logError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MessageFragment : Fragment(R.layout.fragment_message) {
    private val viewModel: MessageViewModel by viewModels()
    private var binding: FragmentMessageBinding? = null
    private val args: MessageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMessageBinding.bind(view)
        setupMessages()
        setupSendMessageButton()
    }

    private fun setupSendMessageButton() {
        binding?.sendMessageButton?.setOnClickListener {
            val messageText = binding?.messageInput?.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(args.userId, messageText)
                binding?.messageInput?.setText("")
                lifecycleScope.launch(Dispatchers.IO) {
                    val lastPosition = viewModel.messageList.count() + 1
                    logError(Exception("$lastPosition"))
                    withContext(Dispatchers.Main) {
                        binding?.messageRv?.scrollToPosition(lastPosition)
                    }
                }
            }
        }
    }

    private fun setupMessages() {
        collect(viewModel.messageList) { messages ->
            binding?.messageRv?.withModels {
                when (messages) {
                    is Result.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Couldn't load messages",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding?.loadingIndicator?.text = "No messages here sorry :p"
                    }

                    Result.Loading -> {
                        binding?.loadingIndicator?.isVisible = true
                    }

                    is Result.Success -> {
                        binding?.loadingIndicator?.isVisible = false
                        messages.data.forEach { message ->

                            if (message.senderUserId == viewModel.myUserId) {
                                senderMessage {
                                    id(message.id)
                                    message(message)
                                }
                            } else {
                                recipientMessage {
                                    id(message.id)
                                    message(message)
                                }
                            }

                            message.replies.forEach { reply ->
                                if (reply.user.id == viewModel.myUserId) {
                                    senderMessage {
                                        id(reply.id)
                                        message(
                                            Message(
                                                id = reply.id,
                                                senderUserId = reply.user.id,
                                                message = reply.message,
                                                createdAt = reply.createdAt,
                                                recipient = message.recipient,
                                                messenger = message.messenger
                                            )
                                        )
                                    }
                                } else {
                                    recipientMessage {
                                        id(reply.id)
                                        message(
                                            Message(
                                                id = reply.id,
                                                senderUserId = reply.user.id,
                                                message = reply.message,
                                                createdAt = reply.createdAt,
                                                recipient = message.recipient,
                                                messenger = message.messenger
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
