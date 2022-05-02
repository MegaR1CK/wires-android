package com.wires.app.presentation.pickusers

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.data.LoadableResult
import com.wires.app.databinding.FragmentPickUsersBinding
import com.wires.app.extensions.addOrRemove
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.showSnackbar
import com.wires.app.extensions.showSoftKeyboard
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class PickUsersFragment : BaseFragment(R.layout.fragment_pick_users) {

    private val binding by viewBinding(FragmentPickUsersBinding::bind)
    private val viewModel: PickUsersViewModel by appViewModels()

    @Inject lateinit var foundUsersAdapter: FoundUsersAdapter

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        stateViewFlipperPickUsers.setStateFromResult(LoadableResult.success(null))
        stateViewFlipperPickUsers.setRetryMethod { viewModel.search() }
        toolbarPickUsers.setNavigationOnClickListener {
            if (linearLayoutPickUsersSearch.isVisible) {
                linearLayoutPickUsersSearch.isVisible = false
            } else {
                findNavController().popBackStack()
            }
        }
        buttonPickUsersSearch.setOnClickListener {
            linearLayoutPickUsersSearch.isVisible = true
            editTextPickUsersSearch.requestFocus()
            requireActivity().showSoftKeyboard()
        }
        buttonPickUsersClear.setOnClickListener { editTextPickUsersSearch.text = null }
        editTextPickUsersSearch.setOnEditorActionListener { _, _, _ ->
            viewModel.search(editTextPickUsersSearch.text?.toString())
            false
        }
        recyclerViewFoundUsers.adapter = foundUsersAdapter.apply {
            onItemClick = { userId -> viewModel.pickedUsersIds.addOrRemove(userId) }
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        searchResultLiveData.observe { result ->
            binding.stateViewFlipperPickUsers.setStateFromResult(result)
            result.doOnSuccess { users ->
                foundUsersAdapter.submitList(users)
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
        searchErrorLiveEvent.observe {
            showSnackbar(getString(R.string.error_short_search_query))
        }
    }
}
