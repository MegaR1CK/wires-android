package com.wires.app.presentation.chat

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.Message
import com.wires.app.databinding.FragmentChatBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.load
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class ChatFragment : BaseFragment(R.layout.fragment_chat) {

    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by appViewModels()
    private val args: ChatFragmentArgs by navArgs()

    private lateinit var messagesAdapter: MessagesListAdapter<Message>

    override fun callOperations() {
        viewModel.getChannel(args.channelId)
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) messagesListChat.smoothScrollToPosition(0)
        }
        stateViewFlipperChat.setRetryMethod { callOperations() }
        toolbarChat.setNavigationOnClickListener { findNavController().popBackStack() }
        messageInputChat.setOnSendClickListener { text ->
            viewModel.sendMessage(args.channelId, text)
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        channelLiveData.observe { result ->
            if (!result.isSuccess) binding.stateViewFlipperChat.setStateFromResult(result)
            result.doOnSuccess { channel ->
                binding.toolbarChat.title = channel.name
                getMessages(args.channelId, 0)
            }
            result.doOnFailure { error ->
                Timber.d(error.message)
            }
        }

        messagesLiveData.observe { result ->
            if (messagesAdapter.isEmpty) {
                binding.stateViewFlipperChat.setStateFromResult(result)
                if (result.isSuccess) listenChannel(args.channelId)
            }
            result.doOnSuccess { items ->
                messagesAdapter.addToEnd(items, false)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        userLiveData.observe { result ->
            result.doOnSuccess { userWrapper ->
                userWrapper.user?.getId()?.let(::setupAdapter)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        receiveMessageLiveEvent.observe { result ->
            result.doOnOpen { Timber.i(getString(R.string.open_socket_message, args.channelId)) }
            result.doOnError { error ->
                Timber.e(error)
                binding.stateViewFlipperChat.setStateFromResult(LoadableResult.failure<Channel>(error))
            }
            result.doOnMessage { message -> messagesAdapter.addToStart(message, true) }
        }

        sendMessageLiveEvent.observe { result ->
            binding.messageInputChat.handleResult(result)
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }

    override fun onDestroyView() {
        viewModel.disconnectChannel(args.channelId)
        super.onDestroyView()
    }

    private fun setupAdapter(userId: String) {
        messagesAdapter = MessagesListAdapter<Message>(userId) { imageView, url, _ ->
            imageView.load(url, isCircle = true)
        }.apply {
            setLoadMoreListener { _, offset ->
                // TODO: custom viewholder
                viewModel.getMessages(args.channelId, offset)
            }
        }
        binding.messagesListChat.setAdapter(messagesAdapter)
    }
}
