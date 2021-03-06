package com.wires.app.presentation.pickusers

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.databinding.FragmentPickUsersBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.navigateBack
import com.wires.app.extensions.navigateTo
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showSoftKeyboard
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class PickUsersFragment : BaseFragment(R.layout.fragment_pick_users) {

    companion object {
        const val USERS_CHANGED_RESULT_KEY = "result_users_changed"
        const val USERS_LIST_RESULT_KEY = "result_users_list"
    }

    private val binding by viewBinding(FragmentPickUsersBinding::bind)
    private val viewModel: PickUsersViewModel by appViewModels()
    private val args: PickUsersFragmentArgs by navArgs()

    @Inject lateinit var foundUsersAdapter: UsersAdapter
    @Inject lateinit var addedUsersAdapter: AddedUsersAdapter

    private var isPersonalMode = false

    override fun callOperations() {
        viewModel.getUser()
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        stateViewFlipperPickUsers.setRetryMethod { callOperations() }
        stateViewFlipperPickUsersResult.setStateFromResult(LoadableResult.success(null))
        stateViewFlipperPickUsersResult.setRetryMethod { viewModel.search() }
        toolbarPickUsers.setNavigationOnClickListener {
            if (linearLayoutPickUsersSearch.isVisible) {
                requireActivity().hideSoftKeyboard()
                linearLayoutPickUsersSearch.isVisible = false
            } else {
                navigateBack()
            }
        }
        buttonPickUsersSearch.setOnClickListener { setSearchFocus() }
        buttonPickUsersClear.setOnClickListener {
            editTextPickUsersSearch.text = null
            requireActivity().showSoftKeyboard(editTextPickUsersSearch)
        }
        buttonPickUsersConfirm.setOnClickListener {
            requireActivity().hideSoftKeyboard()
            setFragmentResult(
                requestKey = USERS_CHANGED_RESULT_KEY,
                result = bundleOf(USERS_LIST_RESULT_KEY to viewModel.pickedUsers.toTypedArray())
            )
            navigateBack()
        }
        editTextPickUsersSearch.setOnEditorActionListener { _, _, _ ->
            viewModel.search(editTextPickUsersSearch.text?.toString())
            requireActivity().hideSoftKeyboard()
            true
        }
        isPersonalMode = args.pickedUsers == null
        setupLists()
        setSearchFocus()
    }

    override fun onBindViewModel() = with(viewModel) {
        pickedUsers = args.pickedUsers?.toMutableList() ?: mutableListOf()
        userLiveData.observe { result ->
            binding.stateViewFlipperPickUsers.setStateFromResult(result)
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        searchResultLiveData.observe { result ->
            binding.stateViewFlipperPickUsersResult.isVisible = true
            binding.stateViewFlipperPickUsersResult.setStateFromResult(result)
            result.doOnSuccess { users ->
                foundUsersAdapter.submitList(users)
                foundUsersAdapter.updateSelectedItems(viewModel.pickedUsers)
                binding.textViewFoundUsers.isVisible = true
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        addedUsersLiveData.observe { list ->
            addedUsersAdapter.submitList(list)
            foundUsersAdapter.updateSelectedItems(list)
            with(binding) {
                recyclerViewAddedUsers.scrollToPosition(list.lastIndex)
                buttonPickUsersConfirm.isVisible = (args.pickedUsers?.toSet() ?: emptySet()) != list.toSet()
                buttonPickUsersConfirm.post {
                    recyclerViewFoundUsers.updatePadding(
                        bottom = if (buttonPickUsersConfirm.isVisible) buttonPickUsersConfirm.height else 0 +
                            resources.getDimensionPixelSize(R.dimen.pick_users_list_bottom_padding)
                    )
                }
            }
        }
        searchErrorLiveEvent.observe {
            showSnackbar(getString(R.string.error_short_search_query))
        }
        createChannelLiveEvent.observe { result ->
            binding.progressIndicatorPickUsers.isVisible = result.isLoading
            foundUsersAdapter.setLockItems(result.isLoading)
            result.doOnSuccess { channel ->
                navigateTo(PickUsersFragmentDirections.actionPickUsersFragmentToChatGraph(channel.id, true))
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
    }

    private fun setSearchFocus() = with(binding) {
        linearLayoutPickUsersSearch.isVisible = true
        editTextPickUsersSearch.requestFocus()
        requireActivity().showSoftKeyboard(editTextPickUsersSearch)
    }

    private fun setupLists() = with(binding) {
        frameLayoutAddedUsers.isVisible = !isPersonalMode
        textViewAddedUsers.isVisible = !isPersonalMode
        (recyclerViewFoundUsers.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerViewFoundUsers.adapter = foundUsersAdapter.apply {
            onItemClick = { user ->
                if (isPersonalMode) {
                    viewModel.createChannel(user.id)
                } else {
                    viewModel.proceedUser(user, removeOnly = false)
                }
            }
            checkboxesEnabled = !isPersonalMode
        }
        recyclerViewFoundUsers.emptyView = emptyViewFoundUsers
        recyclerViewAddedUsers.adapter = addedUsersAdapter.apply {
            onCancelClick = { user -> viewModel.proceedUser(user, removeOnly = true) }
        }
        recyclerViewAddedUsers.emptyView = emptyViewAddedUsers
    }
}
