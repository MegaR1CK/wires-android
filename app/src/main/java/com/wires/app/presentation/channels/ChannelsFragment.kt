package com.wires.app.presentation.channels

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.model.ChannelType
import com.wires.app.data.model.Message
import com.wires.app.databinding.FragmentChannelsBinding
import com.wires.app.extensions.addLinearSpaceItemDecoration
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.setupScrollWithAppBar
import com.wires.app.extensions.showPopupMenu
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.chat.ChatFragment
import timber.log.Timber
import javax.inject.Inject

class ChannelsFragment : BaseFragment(R.layout.fragment_channels) {

    private val binding by viewBinding(FragmentChannelsBinding::bind)
    private val viewModel: ChannelsViewModel by appViewModels()

    @Inject lateinit var channelsAdapter: ChannelsAdapter

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getChannels()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        stateViewFlipperChannels.setRetryMethod { callOperations() }
        buttonChannelsCreate.setOnClickListener { button ->
            button.showPopupMenu { itemId ->
                when (itemId) {
                    R.id.channelCreateActionPersonal -> viewModel.openCreateChannel(ChannelType.PERSONAL)
                    R.id.channelCreateActionGroup -> viewModel.openCreateChannel(ChannelType.GROUP)
                }
            }
        }
        setupChannelsList()
        setupResultListeners()
    }

    override fun onBindViewModel() = with(viewModel) {
        channelsLiveData.observe { result ->
            binding.stateViewFlipperChannels.setStateFromResult(result)
            binding.buttonChannelsCreate.isVisible = result.isSuccess
            result.doOnSuccess { items ->
                binding.emptyViewChannelsList.isVisible = items.isEmpty()
                channelsAdapter.submitList(items)
                channelsAdapter.sortByLastMessageDate()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        openCreateChannelLiveEvent.observe { type ->
            navigateTo(
                if (type == ChannelType.GROUP) {
                    ChannelsFragmentDirections.actionChannelsFragmentToCreateChannelGraph()
                } else {
                    ChannelsFragmentDirections.actionChannelFragmentToPickUsersGraph(null)
                }
            )
        }
        openChatLiveEvent.observe { params ->
            navigateTo(ChannelsFragmentDirections.actionChannelFragmentToChatGraph(params.channelId, params.unreadMessagesCount))
        }
    }

    private fun setupChannelsList() = with(binding.recyclerViewChannels) {
        adapter = channelsAdapter.apply {
            onItemClick = { id, unreadMessages ->
                viewModel.openChat(id, unreadMessages)
            }
        }
        setupScrollWithAppBar(binding.appBarLayoutChannels, binding.root)
        addLinearSpaceItemDecoration(R.dimen.channels_items_spacing)
    }

    private fun setupResultListeners() {
        setFragmentResultListener(ChatFragment.LAST_MESSAGE_CHANGED_RESULT_KEY) { _, bundle ->
            (bundle.getSerializable(ChatFragment.LAST_MESSAGE_RESULT_KEY) as? Message)?.let { message ->
                channelsAdapter.updateLastSentMessage(bundle.getInt(ChatFragment.CHANNEL_ID_RESULT_KEY), message)
                channelsAdapter.sortByLastMessageDate()
            }
        }
        setFragmentResultListener(ChatFragment.UNREAD_MESSAGES_CHANGED_RESULT_KEY) { _, bundle ->
            channelsAdapter.updateUnreadMessages(
                bundle.getInt(ChatFragment.CHANNEL_ID_RESULT_KEY),
                bundle.getInt(ChatFragment.UNREAD_MESSAGES_RESULT_KEY)
            )
        }
        setFragmentResultListener(ChatFragment.CHATS_CHANGED_RESULT_KEY) { _, _ ->
            channelsAdapter.submitList(emptyList())
            callOperations()
        }
    }
}
