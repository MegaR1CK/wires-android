package com.wires.app.presentation.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wires.app.databinding.FragmentRegisterBinding
import com.wires.app.extensions.fitKeyboardInsetsWithPadding
import com.wires.app.extensions.getInputText
import com.wires.app.extensions.hideSoftKeyboard
import com.wires.app.presentation.base.BaseFragment
import timber.log.Timber

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: RegisterViewModel by appViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegisterBinding
        get() = { inflater, viewGroup, attachToRoot ->
            FragmentRegisterBinding.inflate(inflater, viewGroup, attachToRoot)
        }

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

        buttonRegister.setOnClickListener {
            hideSoftKeyboard()
            viewModel.registerUser(
                editTextRegisterEmail.getInputText(),
                editTextRegisterPassword.getInputText(),
                editTextRegisterPasswordRepeat.getInputText(),
                editTextRegisterUsername.getInputText()
            )
        }
    }

    override fun onBindViewModel() = with(viewModel) {
        emailErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterEmail.error = errorRes?.let { getString(it) }
        }
        passwordErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterPassword.error = errorRes?.let { getString(it) }
        }
        confirmPasswordErrorLiveData.observe { errorRes ->
            errorRes?.let {
                binding.textInputLayoutRegisterPasswordRepeat.error = getString(it)
                if (binding.editTextRegisterPassword.getInputText().isEmpty()) {
                    binding.textInputLayoutRegisterPassword.error = getString(it)
                }
            }
        }
        usernameErrorLiveData.observe { errorRes ->
            binding.textInputLayoutRegisterUsername.error = errorRes?.let { getString(it) }
        }
        registerLiveEvent.observe { result ->
            result.doOnSuccess {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToFeedGraph())
            }
            result.doOnFailure { error ->
                Timber.e(error.message)
            }
        }
    }
}
