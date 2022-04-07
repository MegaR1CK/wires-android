package com.wires.app.presentation.register

import android.os.Bundle
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentRegisterBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by appViewModels()
    private val binding by viewBinding(FragmentRegisterBinding::bind)

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()

        editTextRegisterEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateEmail(editTextRegisterEmail.getInputText())
        }
        editTextRegisterPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = editTextRegisterPassword.getInputText()
                val repeatedPassword = editTextRegisterPasswordRepeat.getInputText()
                viewModel.validatePassword(password)
                if (repeatedPassword.isNotEmpty()) {
                    viewModel.validateConfirmPassword(password, repeatedPassword)
                }
            }
        }
        editTextRegisterPasswordRepeat.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateConfirmPassword(
                editTextRegisterPasswordRepeat.getInputText(),
                editTextRegisterPasswordRepeat.getInputText()
            )
        }
        editTextRegisterUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateUsername(editTextRegisterUsername.getInputText())
        }
        editTextRegisterPasswordRepeat.setOnEditorActionListener { _, _, _ ->
            performRegister()
            true
        }
        buttonRegister.setOnClickListener { performRegister() }
    }

    override fun onBindViewModel() = with(viewModel) {
        emailErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterEmail.error = errorRes?.let { getString(it) }
        }
        passwordErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterPassword.error = errorRes?.let { getString(it) }
        }
        confirmPasswordErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterPasswordRepeat.error = errorRes?.let { getString(it) }
            if (binding.textInputLayoutRegisterPassword.error.isNullOrEmpty()) {
                binding.textInputLayoutRegisterPassword.error = errorRes?.let { getString(it) }
            }
        }
        usernameErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterUsername.error = errorRes?.let { getString(it) }
        }
        registerLiveEvent.observe { result ->
            binding.buttonRegister.isLoading = result.isLoading
            result.doOnSuccess {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToFeedGraph())
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
    }

    private fun performRegister() = with(binding) {
        hideSoftKeyboard()
        linearLayoutRegister.children.forEach { it.clearFocus() }
        viewModel.registerUser(
            editTextRegisterEmail.getInputText(),
            editTextRegisterPassword.getInputText(),
            editTextRegisterPasswordRepeat.getInputText(),
            editTextRegisterUsername.getInputText()
        )
    }
}
