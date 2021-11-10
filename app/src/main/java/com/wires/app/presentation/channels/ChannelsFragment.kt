package com.wires.app.presentation.channels

import android.os.Bundle
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

    private val binding by viewBinding(FragmentChannelsBinding::bind)
    private val viewModel: ChannelsViewModel by appViewModels()

    lateinit var channelsAdapter: DialogsListAdapter<Channel>

    override val showBottomNavigationView = true

    override fun callOperations() {
        viewModel.getChannels()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        channelsAdapter = DialogsListAdapter<Channel> { imageView, url, _ ->
            imageView.load(url, isCircle = true)
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
    }
}
