package com.wires.app.presentation.createchannel

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.data.model.Channel
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.FragmentCreateChannelBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getDrawableCompat
import com.wires.app.extensions.handleImagePickerResult
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.load
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.pickImage
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.pickusers.PickUsersFragment
import com.wires.app.presentation.pickusers.UsersAdapter
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CreateChannelFragment : BaseFragment(R.layout.fragment_create_channel) {

    companion object {
        const val CHANNEL_UPDATED_RESULT_KEY = "channel_updated_key"
        const val CHANNEL_NAME_RESULT_KEY = "channel_name_key"
    }

    private val binding by viewBinding(FragmentCreateChannelBinding::bind)
    private val viewModel: CreateChannelViewModel by appViewModels()
    private val args: CreateChannelFragmentArgs by navArgs()

    private val pickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        requireActivity().handleImagePickerResult(
            result = result,
            needPath = true,
            onSuccess = { path ->
                selectedImageUri = Uri.fromFile(File(path)).toString()
                viewModel.selectedImagePath = path
            },
            onFailure = { showSnackbar(getString(R.string.message_image_pick_error)) },
            onComplete = { setChannelImage(selectedImageUri) }
        )
    }

    private var selectedImageUri: String? = null
    private var isEditMode = false
    private var isFirstLaunch = true

    @Inject lateinit var usersAdapter: UsersAdapter

    override fun callOperations() {
        isEditMode = args.channelId != 0
        if (isEditMode) viewModel.getChannel(args.channelId)
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        if (!isEditMode) stateViewFlipperCreateChannel.setStateFromResult(LoadableResult.success(null))
        root.fitKeyboardInsetsWithPadding()
        toolbarCreateChannel.setNavigationOnClickListener { navigateBack() }
        toolbarCreateChannel.title =
            getString(if (isEditMode) R.string.create_channel_edit_mode_title else R.string.create_channel_group_title)
        recyclerViewCreateChannelUsers.adapter = usersAdapter.apply {
            switchButtonState(isEmpty)
            submitList(viewModel.usersInChannel)
            buttonCreateChannelDone.isVisible = itemCount != 0
        }
        recyclerViewCreateChannelUsers.emptyView = emptyViewCreateChannelUsers
        setChannelImage(selectedImageUri)
        buttonCreateChannelEdit.setOnClickListener { viewModel.openPickUsers() }
        viewImagePickerCreateChannel.setOnClickListener {
            requireActivity().pickImage(pickerResultLauncher, needCrop = true)
        }
        inputCreateChannelName.setOnEditorActionListener {
            clearInputFocus()
            true
        }
        buttonCreateChannelDone.setOnClickListener {
            clearInputFocus()
            if (inputCreateChannelName.validate()) {
                val channelName = inputCreateChannelName.text.orEmpty()
                if (isEditMode) viewModel.editChannel(args.channelId, channelName) else viewModel.createChannel(channelName)
            }
        }
        setFragmentResultListener(PickUsersFragment.USERS_CHANGED_RESULT_KEY) { _, bundle ->
            bundle.getParcelableArray(PickUsersFragment.USERS_LIST_RESULT_KEY)
                ?.mapNotNull { it as? UserPreview }
                ?.let(::setUsersInChannel)
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        channelLiveData.observe { result ->
            binding.stateViewFlipperCreateChannel.setStateFromResult(result)
            result.doOnSuccess { channel ->
                if (isFirstLaunch) {
                    bindChannel(channel)
                    isFirstLaunch = false
                }
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }

        createChannelLiveEvent.observe { result ->
            binding.progressIndicatorCreateChannel.isVisible = result.isLoading
            result.doOnSuccess { channel ->
                navigateTo(CreateChannelFragmentDirections.actionCreateChannelFragmentToChatGraph(channel.id, true))
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }

        editChannelLiveEvent.observe { result ->
            binding.progressIndicatorCreateChannel.isVisible = result.isLoading
            binding.inputCreateChannelName.editText.isEnabled = !result.isLoading
            result.doOnSuccess {
                setFragmentResult(
                    requestKey = CHANNEL_UPDATED_RESULT_KEY,
                    result = bundleOf(CHANNEL_NAME_RESULT_KEY to binding.inputCreateChannelName.text)
                )
                navigateBack()
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }

        openPickUsersLiveEvent.observe {
            navigateTo(
                CreateChannelFragmentDirections
                    .actionCreateChannelFragmentToPickUsersGraph(viewModel.usersInChannel.toTypedArray())
            )
        }
    }

    private fun bindChannel(channel: Channel) = with(binding) {
        setChannelImage(channel.image?.url)
        inputCreateChannelName.text = channel.name
        setUsersInChannel(channel.members)
    }

    private fun switchButtonState(isListEmpty: Boolean) = with(binding.buttonCreateChannelEdit) {
        icon = requireContext().getDrawableCompat(if (isListEmpty) R.drawable.ic_add else R.drawable.ic_edit)
        text = getString(if (isListEmpty) R.string.create_channel_add_users else R.string.create_channel_edit_users)
    }

    private fun setChannelImage(uri: String?) = with(binding) {
        imageViewCreateChannel.load(uri)
        imageViewCreateChannelIcon.setColorFilter(
            requireContext().getColorAttribute(if (uri != null) R.attr.iconColorOnContrast else R.attr.colorPrimary)
        )
    }

    private fun clearInputFocus() {
        requireActivity().hideSoftKeyboard()
        binding.inputCreateChannelName.editText.clearFocus()
    }

    private fun setUsersInChannel(usersInChannel: List<UserPreview>) {
        usersAdapter.submitList(usersInChannel)
        viewModel.usersInChannel = usersInChannel
        switchButtonState(usersInChannel.isEmpty())
        binding.buttonCreateChannelDone.isVisible = usersInChannel.isNotEmpty()
    }
}
