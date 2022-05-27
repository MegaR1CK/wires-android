package com.wires.app.presentation.chat

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        const val UNREAD_MESSAGES_CHANGED_RESULT_KEY = "unread_messages_changed"
        const val UNREAD_MESSAGES_RESULT_KEY = "unread_messages"

        /** Количество сообщений, гарантированно видимое на экране */
        private const val VISIBLE_MESSAGES_COUNT = 3

        /** Максимальное количество непрочитанных сообщений, при котором не нужно увеличивать лимит пагинации */
        private const val UNREAD_MESSAGES_WITHOUT_PAGINATION = 5

        /**
         *  Дополнительный прирост лимита пагинации для создания буфера
         *  прогруженных сообщений при скролле к первому непрочитанному
         */
        private const val ADDITIONAL_LIMIT = 15
    }

    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by appViewModels()
    private val args: ChatFragmentArgs by navArgs()

    private var initialMessageSent = false
    /** Позиция последнего прочитанного сообщения в канале */
    private var lastReadMessagePosition = 0

    @Inject lateinit var messagesAdapter: MessagesAdapter

    override fun callOperations() {
        viewModel.getChannel(args.channelId)
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding { _, insets, _ ->
            if (insets.getKeyboardInset() > 0) recyclerViewMessages.smoothScrollToPosition(0)
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
                getMessages(
                    args.channelId,
                    limit = args.unreadMessagesCount.takeIf { it > UNREAD_MESSAGES_WITHOUT_PAGINATION }?.plus(ADDITIONAL_LIMIT)
                )
            }
            result.doOnFailure { error ->
                Timber.d(error.message)
            }
        }

        messagesLiveData.observe { result ->
            if (messagesAdapter.isEmpty && !result.isSuccess) binding.stateViewFlipperChat.setStateFromResult(result)
            result.doOnSuccess { items ->
                initialMessageSent = items.isNotEmpty()
                val displayingItems = items.filter { !it.isInitial }
                binding.emptyViewMessageList.isVisible = displayingItems.isEmpty() && messagesAdapter.isEmpty
                val isFirstPage = messagesAdapter.isEmpty
                messagesAdapter.addToEnd(displayingItems)
                if (isFirstPage) {
                    listenChannel(args.channelId)
                    val firstUnreadItemId = displayingItems.findLast { !it.isRead }?.id
                    val firstUnreadPosition = firstUnreadItemId?.let { messagesAdapter.getPositionById(it) } ?: -1
                    if (firstUnreadPosition > VISIBLE_MESSAGES_COUNT) {
                        binding.recyclerViewMessages.scrollToPosition(firstUnreadPosition)
                    }
                    lastReadMessagePosition = firstUnreadPosition + 1
                }
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
                    viewModel.messagesIdsForRead.add(message.id)
                    binding.recyclerViewMessages.scrollToPosition(0)
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

    override fun onStop() {
        super.onStop()
        viewModel.readMessages(args.channelId)
        setFragmentResult(
            requestKey = UNREAD_MESSAGES_CHANGED_RESULT_KEY,
            result = bundleOf(
                CHANNEL_ID_RESULT_KEY to args.channelId,
                UNREAD_MESSAGES_RESULT_KEY to args.unreadMessagesCount - viewModel.messagesIdsForRead.size
            )
        )
    }

    override fun onDestroyView() {
        viewModel.disconnectChannel(args.channelId)
        super.onDestroyView()
    }

    private fun setupMessagesList(userId: Int) = with(binding.recyclerViewMessages) {
        addLinearSpaceItemDecoration(R.dimen.chat_items_spacing, showFirstHorizontalDivider = true)
        (layoutManager as? LinearLayoutManager)?.let { layoutManager ->
            val onLoadMoreListener = object : ScrollMoreListener.OnLoadMoreListener {
                override fun getPrimaryItemsCount() = messagesAdapter.messagesCount

                override fun loadMore(offset: Int) {
                    viewModel.getMessages(args.channelId, offset)
                }
            }
            addOnScrollListener(ScrollMoreListener(layoutManager, onLoadMoreListener))
            // Скролл листенер для чтения сообщений по мере прокрутки
            // Состоит из двух режимов:
            // 1 - стандартный ход (чтение сообщений по мере прокрутки списка),
            // 2 - обратный ход (если во время быстрой прокрутки несколько сообщений
            // оказались пропущены, идем по списку вверх, пока не прочитаем их все)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // При скролле находим позицию самого нижнего видимого элемента
                    val currentPosition = (layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                    // Если позиция меньше позиции последнего прочитаннного сообщения (или последнего
                    // прочитанного сообщения не существует), получаем элемент на позиции
                    if (currentPosition != null && (currentPosition < lastReadMessagePosition || lastReadMessagePosition == -1)) {
                        val currentItem = messagesAdapter.getItem(currentPosition)
                        // Если элемент является сообщением, добавляем его id в список для чтения
                        if (currentItem is MessageListItem.ListMessage) {
                            viewModel.messagesIdsForRead.add(currentItem.message.id)
                            // Если пользователь скроллил быстро, есть вероятность, что листенер пропустил некоторые сообщения
                            // Если разница между текущим и последним прочитанным
                            // сообщением больше одного, запускаем обратный ход
                            if (lastReadMessagePosition - currentPosition > 1) {
                                var missedItemPosition = currentPosition + 1
                                var missedItem = messagesAdapter.getItem(missedItemPosition) as? MessageListItem.ListMessage
                                // Пока список для чтения сообщений не содержит id пропущенного сообщения или пока не дошли
                                // до прочитанного сообщения, добавляем его в список и поднимаемся вверх (увеличиваем позицию)
                                while (
                                    !viewModel.messagesIdsForRead.contains(missedItem?.message?.id) &&
                                    missedItem?.message?.isRead != true
                                ) {
                                    missedItem?.message?.id?.let { viewModel.messagesIdsForRead.add(it) }
                                    // Если следующая позиция выходит за границы списка,
                                    // значит мы дошли до конца списка, выходим из цикла
                                    if (++missedItemPosition == messagesAdapter.itemCount) break
                                    missedItem = messagesAdapter.getItem(missedItemPosition) as? MessageListItem.ListMessage
                                }
                            }
                            lastReadMessagePosition = currentPosition
                        }
                    }
                }
            })
        }
        adapter = messagesAdapter.apply {
            senderId = userId
        }
    }

    private fun sendInitialMessage() =
        viewModel.sendMessage(args.channelId, getString(R.string.chat_initial_message_text), isInitial = true)
}
