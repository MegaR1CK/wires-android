package com.wires.app.presentation.changepassword

import android.os.Bundle
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentChangePasswordBinding
import com.wires.app.extensions.fitTopInsetsWithPadding
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class ChangePasswordFragment : BaseFragment(R.layout.fragment_change_password) {

    private val binding by viewBinding(FragmentChangePasswordBinding::bind)
    private val viewModel: ChangePasswordViewModel by appViewModels()

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitTopInsetsWithPadding()
        toolbarChangePassword.setNavigationOnClickListener { findNavController().popBackStack() }
        buttonChangePasswordDone.setOnClickListener {
            hideSoftKeyboard()
            linearLayoutChangePassword.children.forEach { it.clearFocus() }
            val oldPasswordValidated = inputOldPassword.validate()
            val newPasswordValidated = inputNewPassword.validate()
            val newPasswordConfirmValidated = inputNewPasswordConfirm.validate()
            if (oldPasswordValidated && newPasswordValidated && newPasswordConfirmValidated) {
                viewModel.changePassword(inputOldPassword.text.orEmpty(), inputNewPassword.text.orEmpty())
            }
        }
        inputNewPassword.additionalValidation = { text ->
            if (!inputNewPasswordConfirm.isEmpty()) text == inputNewPasswordConfirm.text else true
        }
        inputNewPasswordConfirm.additionalValidation = { text ->
            if (!inputNewPassword.isEmpty()) text == inputNewPassword.text else true
        }
        inputNewPassword.onFocusRemoved = {
            if (!inputNewPasswordConfirm.isEmpty()) inputNewPasswordConfirm.validate()
        }
        inputNewPasswordConfirm.onFocusRemoved = {
            if (!inputNewPassword.isEmpty()) inputNewPassword.validate()
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        changePasswordLiveEvent.observe { result ->
            binding.progressIndicatorChangePassword.isVisible = result.isLoading
            result.doOnSuccess {
                showSnackbar(getString(R.string.change_password_success))
                findNavController().popBackStack()
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
                showSnackbar(error.message)
            }
        }
    }
}
