package com.wires.app.presentation.login

import android.os.Bundle
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentLoginBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by appViewModels()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        editTextLoginEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateEmail(editTextLoginEmail.getInputText())
        }
        editTextLoginPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validatePassword(editTextLoginPassword.getInputText())
        }
        editTextLoginPassword.setOnEditorActionListener { _, _, _ ->
            performLogin()
            true
        }
        buttonLogin.setOnClickListener { performLogin() }
    }

    override fun onBindViewModel() = with(viewModel) {
        emailErrorLiveData.observe { errorRes ->
            binding.textInputLayoutLoginEmail.error = errorRes?.let { getString(it) }
        }
        passwordErrorLiveData.observe { errorRes ->
            binding.textInputLayoutLoginPassword.error = errorRes?.let { getString(it) }
        }
        loginLiveEvent.observe { result ->
            binding.buttonLogin.isLoading = result.isLoading
            result.doOnSuccess {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedGraph())
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }

    private fun performLogin() = with(binding) {
        hideSoftKeyboard()
        linearLayoutLogin.children.forEach { it.clearFocus() }
        viewModel.login(editTextLoginEmail.getInputText(), editTextLoginPassword.getInputText())
    }
}
