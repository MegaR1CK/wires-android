package com.wires.app.presentation.pickusers

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.databinding.FragmentPickUsersBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showSoftKeyboard
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class PickUsersFragment : BaseFragment(R.layout.fragment_pick_users) {

    private val binding by viewBinding(FragmentPickUsersBinding::bind)
    private val viewModel: PickUsersViewModel by appViewModels()

    @Inject lateinit var foundUsersAdapter: FoundUsersAdapter
    @Inject lateinit var addedUsersAdapter: AddedUsersAdapter

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        stateViewFlipperPickUsers.setStateFromResult(LoadableResult.success(null))
        stateViewFlipperPickUsers.setRetryMethod { viewModel.search() }
        toolbarPickUsers.setNavigationOnClickListener {
            if (linearLayoutPickUsersSearch.isVisible) {
                requireActivity().hideSoftKeyboard()
                linearLayoutPickUsersSearch.isVisible = false
            } else {
                findNavController().popBackStack()
            }
        }
        buttonPickUsersSearch.setOnClickListener { setSearchFocus() }
        buttonPickUsersClear.setOnClickListener {
            editTextPickUsersSearch.text = null
            requireActivity().showSoftKeyboard()
        }
        editTextPickUsersSearch.setOnEditorActionListener { _, _, _ ->
            viewModel.search(editTextPickUsersSearch.text?.toString())
            requireActivity().hideSoftKeyboard()
            true
        }
        (recyclerViewFoundUsers.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerViewFoundUsers.adapter = foundUsersAdapter.apply {
            onItemClick = { user -> viewModel.proceedUser(user, removeOnly = false) }
        }
        recyclerViewAddedUsers.adapter = addedUsersAdapter.apply {
            onCancelClick = { user -> viewModel.proceedUser(user, removeOnly = true) }
        }
        setSearchFocus()
    }

    override fun onBindViewModel() = with(viewModel) {
        searchResultLiveData.observe { result ->
            binding.stateViewFlipperPickUsers.setStateFromResult(result)
            result.doOnSuccess { users ->
                foundUsersAdapter.submitList(users)
                foundUsersAdapter.updateSelectedItems(viewModel.pickedUsers)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        addedUsersLiveData.observe { list ->
            addedUsersAdapter.submitList(list)
            foundUsersAdapter.updateSelectedItems(list)
            binding.recyclerViewAddedUsers.scrollToPosition(list.lastIndex)
        }
        searchErrorLiveEvent.observe {
            showSnackbar(getString(R.string.error_short_search_query))
        }
    }

    private fun setSearchFocus() = with(binding) {
        linearLayoutPickUsersSearch.isVisible = true
        editTextPickUsersSearch.requestFocus()
        requireActivity().showSoftKeyboard()
    }
}
