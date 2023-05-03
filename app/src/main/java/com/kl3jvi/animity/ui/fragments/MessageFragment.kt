package com.kl3jvi.animity.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.Message
import com.kl3jvi.animity.databinding.FragmentMessageBinding
import com.kl3jvi.animity.recipientMessage
import com.kl3jvi.animity.senderMessage
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import org.joda.time.LocalDate

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
                lifecycleScope.launch {
                    val lastPosition = viewModel.messageList.count() + 1
                    binding?.messageRv?.scrollToPosition(lastPosition)
                }
            }
        }
    }

    private fun setupMessages() {
        collect(viewModel.messageList) { messages ->
            Log.e("Messages", messages.toString())
            binding?.messageRv?.withModels {
                val messagesByDate = messages.groupBy {
                    LocalDate(it.createdAt.toLong() / 86400) // Convert epoch to date and group by date
                }
                messagesByDate.forEach { (date, messages) ->
                    Log.e(date.dayOfWeek.toString(), messages.toString())
                }

                messages.forEach { message ->
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
