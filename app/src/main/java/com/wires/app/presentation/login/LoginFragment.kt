package com.wires.app.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wires.app.databinding.FragmentLoginBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.extensions.showToast
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by appViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentLoginBinding.inflate(inflater, viewGroup, attachToRoot)
        }

    override fun callOperations() = Unit

    override fun onSetupLayout(savedInstanceState: Bundle?) = with(binding) {
        root.fitKeyboardInsetsWithPadding()
        editTextLoginEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validateEmail(editTextLoginEmail.getInputText())
        }
        editTextLoginPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.validatePassword(editTextLoginPassword.getInputText())
        }
        buttonLogin.setOnClickListener {
            hideSoftKeyboard()
            viewModel.login(editTextLoginEmail.getInputText(), editTextLoginPassword.getInputText())
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        emailErrorLiveData.observe { errorRes ->
            binding.textInputLayoutLoginEmail.error = errorRes?.let { getString(it) }
        }
        passwordErrorLiveData.observe { errorRes ->
            binding.textInputLayoutLoginPassword.error = errorRes?.let { getString(it) }
        }
        loginLiveEvent.observe { result ->
            result.doOnSuccess {
                showToast("Success")
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }
}
