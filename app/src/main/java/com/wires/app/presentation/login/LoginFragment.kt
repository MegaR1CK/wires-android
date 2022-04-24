package com.wires.app.presentation.login

import android.os.Bundle
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wires.app.R
import com.wires.app.databinding.FragmentLoginBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showSnackbar
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by appViewModels()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        inputLoginPassword.setOnEditorActionListener {
            performLogin()
            true
        }
        buttonLogin.setOnClickListener { performLogin() }
    }

    override fun onBindViewModel() = with(viewModel) {
        loginLiveEvent.observe { result ->
            binding.buttonLogin.isLoading = result.isLoading
            result.doOnSuccess {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedGraph())
            }
            result.doOnFailure { error ->
                showSnackbar(error.message)
                Timber.e(error.message)
            }
        }
    }

    private fun performLogin() = with(binding) {
        hideSoftKeyboard()
        linearLayoutLogin.children.forEach { it.clearFocus() }
        val emailValidated = inputLoginEmail.validate()
        val passwordValidated = inputLoginPassword.validate()
        if (emailValidated && passwordValidated) {
            viewModel.login(inputLoginEmail.text.orEmpty(), inputLoginPassword.text.orEmpty())
        }
    }
}
