package com.wires.app.presentation.chat

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.databinding.FragmentChatBinding
import com.wires.app.extensions.addLinearSpaceItemDecoration
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getKeyboardInset
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class ChatFragment : BaseFragment(R.layout.fragment_chat) {

    companion object {
        const val LAST_MESSAGE_CHANGED_RESULT_KEY = "result_last_message_changed"
        const val LAST_MESSAGE_RESULT_KEY = "result_last_message"
        const val CHANNEL_ID_RESULT_KEY = "result_channel_id"
        const val CHATS_CHANGED_RESULT_KEY = "result_chats_changed"
    }

    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by appViewModels()
    private val args: ChatFragmentArgs by navArgs()

    private var initialMessageSent = false

    @Inject lateinit var messagesAdapter: MessagesAdapter

    override fun callOperations() {
        viewModel.getChannel(args.channelId)
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) messagesListChat.smoothScrollToPosition(0)
        }
        stateViewFlipperChat.setRetryMethod { callOperations() }
        toolbarChat.setNavigationOnClickListener { navigateBack() }
        messageInputChat.setOnSendClickListener { text ->
            viewModel.sendMessage(args.channelId, text, isInitial = false)
        }
        if (args.isNew) setFragmentResult(CHATS_CHANGED_RESULT_KEY, bundleOf())
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
                if (!result.isSuccess) binding.stateViewFlipperChat.setStateFromResult(result) else listenChannel(args.channelId)
            }
            result.doOnSuccess { items ->
                initialMessageSent = items.isNotEmpty()
                val displayingItems = items.filter { !it.isInitial }
                binding.emptyViewMessageList.isVisible = displayingItems.isEmpty() && messagesAdapter.isEmpty
                messagesAdapter.addToEnd(displayingItems)
            }
            result.doOnFailure { error ->
                if (!messagesAdapter.isEmpty) showSnackbar(error.message)
                Timber.e(error.message)
            }
        }

        userLiveData.observe { result ->
            result.doOnSuccess { userWrapper ->
                userWrapper.user?.id?.let(::setupMessagesList)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        receiveMessageLiveEvent.observe { result ->
            result.doOnOpen {
                binding.stateViewFlipperChat.setStateFromResult(LoadableResult.success(null))
                Timber.i(getString(R.string.open_socket_message, args.channelId))
                if (!initialMessageSent) {
                    sendInitialMessage()
                    initialMessageSent = true
                }
            }
            result.doOnError { error ->
                Timber.e(error)
                showSnackbar(getString(R.string.error_no_network_description))
            }
            result.doOnMessage { message ->
                if (!message.isInitial) {
                    messagesAdapter.addNewMessage(message)
                    binding.messagesListChat.scrollToPosition(0)
                }
                binding.emptyViewMessageList.isVisible = messagesAdapter.isEmpty
                setFragmentResult(
                    requestKey = LAST_MESSAGE_CHANGED_RESULT_KEY,
                    result = bundleOf(CHANNEL_ID_RESULT_KEY to args.channelId, LAST_MESSAGE_RESULT_KEY to message)
                )
            }
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

    private fun setupMessagesList(userId: Int) = with(binding.messagesListChat) {
        addLinearSpaceItemDecoration(R.dimen.chat_items_spacing, showFirstHorizontalDivider = true)
        (layoutManager as? LinearLayoutManager)?.let { layoutManager ->
            addOnScrollListener(ScrollMoreListener(layoutManager) { viewModel.getMessages(args.channelId, it) })
        }
        adapter = messagesAdapter.apply {
            senderId = userId
        }
    }

    private fun sendInitialMessage() =
        viewModel.sendMessage(args.channelId, getString(R.string.chat_initial_message_text), isInitial = true)
}
