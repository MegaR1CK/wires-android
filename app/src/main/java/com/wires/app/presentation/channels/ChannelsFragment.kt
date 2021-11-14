package com.wires.app.presentation.channels

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.wires.app.R
import com.wires.app.data.model.Channel
import com.wires.app.databinding.FragmentChannelsBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.load
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class ChannelsFragment : BaseFragment(R.layout.fragment_channels) {

    companion object {
        private const val MAX_CHAT_TITLE_MEMBERS = 3
    }

    private val binding by viewBinding(FragmentChannelsBinding::bind)
    private val viewModel: ChannelsViewModel by appViewModels()

    private lateinit var channelsAdapter: DialogsListAdapter<Channel>

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getChannels()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        channelsAdapter = DialogsListAdapter<Channel> { imageView, url, _ ->
            imageView.load(url, isCircle = true)
        }.apply {
            setOnDialogClickListener { channel ->
                viewModel.openChat(
                    channelId = channel.id,
                    channelName = channel.name ?: channel.members.joinToString(limit = MAX_CHAT_TITLE_MEMBERS)
                )
            }
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        channelsLiveData.observe { result ->
            binding.stateViewFlipperChannels.setStateFromResult(result)
            result.doOnSuccess { items ->
                binding.channelsListChannels.setAdapter(channelsAdapter)
                channelsAdapter.setItems(items)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        openChatLiveEvent.observe { params ->
            findNavController().navigate(
                ChannelsFragmentDirections.actionChannelFragmentToChatFragment(
                    params.channelId,
                    params.channelName
                )
            )
        }
    }
}
