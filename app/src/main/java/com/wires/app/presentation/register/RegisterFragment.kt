package com.wires.app.presentation.register

import android.os.Bundle
import android.util.Patterns
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentRegisterBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
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
        inputRegisterEmail.validationRegex = Patterns.EMAIL_ADDRESS.toRegex()
        inputRegisterConfirmPassword.setOnEditorActionListener {
            performRegister()
            true
        }
        inputRegisterConfirmPassword.additionalValidation = { text ->
            text == inputRegisterPassword.text
        }
        inputRegisterPassword.additionalValidation = { text ->
            text == inputRegisterConfirmPassword.text
        }
        inputRegisterConfirmPassword.onFocusRemoved = {
            inputRegisterPassword.validate()
        }
        inputRegisterPassword.onFocusRemoved = {
            inputRegisterConfirmPassword.validate()
        }
        buttonRegister.setOnClickListener { performRegister() }
    }

    override fun onBindViewModel() = with(viewModel) {
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
        val usernameValidated = inputRegisterUsername.validate()
        val emailValidated = inputRegisterEmail.validate()
        val passwordValidated = inputRegisterPassword.validate()
        val confirmPasswordValidated = inputRegisterConfirmPassword.validate()
        if (usernameValidated && emailValidated && passwordValidated && confirmPasswordValidated) {
            viewModel.registerUser(
                inputRegisterUsername.text.orEmpty(),
                inputRegisterEmail.text.orEmpty(),
                inputRegisterPassword.text.orEmpty(),
            )
        }
    }
}
