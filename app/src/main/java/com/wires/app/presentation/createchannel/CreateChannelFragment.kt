package com.wires.app.presentation.createchannel

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wires.app.R
import com.wires.app.data.model.UserPreview
import com.wires.app.databinding.FragmentCreateChannelBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getColorAttribute
import com.wires.app.extensions.getDrawableCompat
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import com.wires.app.presentation.pickusers.PickUsersFragment
import com.wires.app.presentation.pickusers.UsersAdapter
import timber.log.Timber
import javax.inject.Inject

class CreateChannelFragment : BaseFragment(R.layout.fragment_create_channel) {

    private val binding by viewBinding(FragmentCreateChannelBinding::bind)
    private val viewModel: CreateChannelViewModel by appViewModels()

    private var selectedImageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                viewModel.selectedImagePath = uri.path
            }
        }
        setChannelImage(selectedImageUri)
    }

    @Inject lateinit var usersAdapter: UsersAdapter

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        toolbarCreateChannel.setNavigationOnClickListener { findNavController().popBackStack() }
        recyclerViewCreateChannelUsers.adapter = usersAdapter.apply {
            switchButtonState(itemCount == 0)
            submitList(viewModel.usersInChannel)
            buttonCreateChannelDone.isVisible = itemCount != 0
        }
        recyclerViewCreateChannelUsers.emptyView = emptyViewCreateChannelUsers
        setChannelImage(selectedImageUri)
        buttonCreateChannelEdit.setOnClickListener { viewModel.openPickUsers() }
        viewImagePickerCreateChannel.setOnClickListener {
            ImagePicker
                .with(requireActivity())
                .cropSquare()
                .createIntent { resultLauncher.launch(it) }
        }
        inputCreateChannelName.setOnEditorActionListener {
            clearInputFocus()
            true
        }
        buttonCreateChannelDone.setOnClickListener {
            clearInputFocus()
            if (inputCreateChannelName.validate()) viewModel.createChannel(inputCreateChannelName.text.orEmpty())
        }
        setFragmentResultListener(PickUsersFragment.USERS_CHANGED_RESULT_KEY) { _, bundle ->
            bundle.getParcelableArray(PickUsersFragment.USERS_LIST_RESULT_KEY)
                ?.mapNotNull { it as? UserPreview }?.let { userList ->
                    usersAdapter.submitList(userList)
                    viewModel.usersInChannel = userList
                    switchButtonState(userList.isEmpty())
                    buttonCreateChannelDone.isVisible = userList.isNotEmpty()
                }
        }
    }
    override fun onBindViewModel() = with(viewModel) {
        createChannelLiveEvent.observe { result ->
            binding.progressIndicatorCreateChannel.isVisible = result.isLoading
            result.doOnSuccess { channel ->
                findNavController()
                    .navigate(CreateChannelFragmentDirections.actionCreateChannelFragmentToChatGraph(channel.id, true))
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
        openPickUsersLiveEvent.observe {
            findNavController().navigate(
                CreateChannelFragmentDirections
                    .actionCreateChannelFragmentToPickUsersGraph(viewModel.usersInChannel.toTypedArray())
            )
        }
    }

    private fun switchButtonState(isListEmpty: Boolean) = with(binding.buttonCreateChannelEdit) {
        icon = requireContext().getDrawableCompat(if (isListEmpty) R.drawable.ic_add else R.drawable.ic_edit)
        text = getString(if (isListEmpty) R.string.create_channel_add_users else R.string.create_channel_edit_users)
    }

    private fun setChannelImage(uri: Uri?) = with(binding) {
        imageViewCreateChannel.setImageURI(uri)
        imageViewCreateChannelIcon.setColorFilter(
            requireContext().getColorAttribute(if (uri != null) R.attr.iconColorOnContrast else R.attr.colorPrimary)
        )
    }

    private fun clearInputFocus() {
        requireActivity().hideSoftKeyboard()
        binding.inputCreateChannelName.editText.clearFocus()
    }
}
