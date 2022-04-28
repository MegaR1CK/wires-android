package com.wires.app.presentation.channels

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.wires.app.R
import com.wires.app.data.model.ChannelPreview
import com.wires.app.data.model.Message
import com.wires.app.databinding.FragmentChannelsBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.load
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.chat.ChatFragment
import timber.log.Timber

class ChannelsFragment : BaseFragment(R.layout.fragment_channels) {

    private val binding by viewBinding(FragmentChannelsBinding::bind)
    private val viewModel: ChannelsViewModel by appViewModels()

    private var channelsAdapter = DialogsListAdapter<ChannelPreview> { imageView, url, _ ->
        imageView.load(url, isCircle = true)
    }.apply {
        setOnDialogClickListener { channel ->
            viewModel.openChat(channel.id)
        }
    }

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getChannels()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        stateViewFlipperChannels.setRetryMethod { callOperations() }
        setFragmentResultListener(ChatFragment.LAST_MESSAGE_CHANGED_RESULT_KEY) { _, bundle ->
            channelsAdapter.getItemById(bundle.getInt(ChatFragment.CHANNEL_ID_RESULT_KEY).toString())?.let { channel ->
                val updatedChannel =
                    channel.copy(lastSentMessage = bundle.getSerializable(ChatFragment.LAST_MESSAGE_RESULT_KEY) as? Message)
                channelsAdapter.updateItemById(updatedChannel)
            }
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        channelsLiveData.observe { result ->
            binding.stateViewFlipperChannels.setStateFromResult(result)
            result.doOnSuccess { items ->
                binding.channelsListChannels.setAdapter(channelsAdapter)
                if (channelsAdapter.isEmpty) channelsAdapter.setItems(items)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        openChatLiveEvent.observe { channelId ->
            findNavController().navigate(ChannelsFragmentDirections.actionChannelFragmentToChatFragment(channelId))
        }
    }
}
